import pandas as pd
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity


try:
    from data_prep import load_and_preprocess
    from collaborative import train_svd_surprise
    from content_based import build_tfidf_embeddings
    from hybrid import recommend_weighted
    from eval_metrics import precision_at_k, recall_at_k, ndcg_at_k
    from model_manager import save_model, load_model
except ImportError:
    print("Warning: Could not import from project files. Make sure all .py files are in the same directory.")


    # Add dummy functions to allow the script to be read, though it won't run
    def load_and_preprocess(r, p, **kwargs):
        return pd.DataFrame(), pd.DataFrame(), pd.DataFrame(), pd.DataFrame()


    def train_svd_surprise(df):
        return None


    def build_tfidf_embeddings(df, **kwargs):
        return None, np.array([[]])


    def recommend_weighted(**kwargs):
        return []


    def precision_at_k(r, g, k):
        return 0


    def recall_at_k(r, g, k):
        return 0


    def ndcg_at_k(r, g, k):
        return 0


    def save_model(o, n):
        pass

class CollaborativeFiltering:

    def __init__(self, train_df):
        print("Training SVD model (Collaborative Filtering)...")
        self.model = train_svd_surprise(train_df)
        self.all_item_ids = train_df['item_id'].unique()

    def predict_score(self, user_id, item_id):
        return self.model.predict(user_id, item_id).est

    def recommend(self, user_id, train_df, k=10):
        seen_items = set(train_df[train_df['user_id'] == user_id]['item_id'])
        unseen_items = [item for item in self.all_item_ids if item not in seen_items]

        scores = []
        for item_id in unseen_items:
            score = self.predict_score(user_id, item_id)
            scores.append((item_id, score))

        scores.sort(key=lambda x: x[1], reverse=True)
        # Return just the item IDs
        return [item_id for item_id, score in scores[:k]]


class ContentBasedFiltering:

    def __init__(self, products_df):
        print("Building TF-IDF embeddings (Content-Based)...")
        self.products = products_df
        # Create mappings for item_id <-> matrix index
        self.item_id_to_idx = pd.Series(self.products.index, index=self.products.item_id)
        self.idx_to_item_id = pd.Series(self.products.item_id, index=self.products.index)

        # Build TF-IDF
        self.tfidf_model, self.tfidf_matrix = build_tfidf_embeddings(
            self.products,
            text_col='description'
        )
        print("TF-IDF matrix built. Similarity will be calculated on-the-fly.")

    def get_user_profile_scores(self, user_id, train_df):
        # Get user's positively-rated items
        user_history = train_df[
            (train_df['user_id'] == user_id) & (train_df['rating'] >= 4.0)
            ]

        if user_history.empty:
            return {}  # No history, no scores

        # Get matrix indices for these items
        user_item_indices = self.item_id_to_idx[user_history['item_id']].values

        user_vectors = self.tfidf_matrix[user_item_indices]
        sim_scores_to_history = cosine_similarity(user_vectors, self.tfidf_matrix)

        agg_scores = sim_scores_to_history.mean(axis=0)
        return dict(zip(self.idx_to_item_id, agg_scores))

    def recommend_by_user_history(self, user_id, train_df, k=10):
        cb_scores = self.get_user_profile_scores(user_id, train_df)

        if not cb_scores:
            print(f"User {user_id} has no positive rating history for CB.")
            return []

        seen_items = set(train_df[train_df['user_id'] == user_id]['item_id'])
        unseen_item_scores = [
            (item_id, score) for item_id, score in cb_scores.items()
            if item_id not in seen_items
        ]

        unseen_item_scores.sort(key=lambda x: x[1], reverse=True)
        return [item_id for item_id, score in unseen_item_scores[:k]]


class HybridRecommender:

    def __init__(self, cf_model, cb_model, weight_cf=0.6, weight_cb=0.4):
        self.cf = cf_model
        self.cb = cb_model
        self.w_cf = weight_cf
        self.w_cb = weight_cb

    def recommend(self, user_id, train_df, k=10):
        cf_predict_fn = self.cf.predict_score
        cb_scores_dict = self.cb.get_user_profile_scores(user_id, train_df)
        # Luăm primii (k * 10) candidați din fiecare model pentru a avea un bazin de selecție.
        CANDIDATE_POOL_SIZE = k * 10  # Vom lua top 100 de la fiecare
        cf_candidates = self.cf.recommend(user_id, train_df, k=CANDIDATE_POOL_SIZE)
        cb_candidates = self.cb.recommend_by_user_history(user_id, train_df, k=CANDIDATE_POOL_SIZE)
        # Combinăm listele și eliminăm duplicatele
        candidate_items = list(set(cf_candidates) | set(cb_candidates))
        # Folosim `recommend_weighted` pentru Re-ranking
        hybrid_scores = recommend_weighted(
            user_id=user_id,
            candidate_items=candidate_items,
            cf_model_predict_fn=cf_predict_fn,
            cb_scores=cb_scores_dict,
            w_cf=self.w_cf,
            w_cb=self.w_cb,
            top_k=k
            # `recommend_weighted` va sorta și va returna oricum doar primii `top_k`
        )
        return [item_id for item_id, *scores in hybrid_scores]


class Explainability:
    def __init__(self, products_df, cb_model, train_df):
        self.products = products_df.set_index('item_id')
        self.cb = cb_model  # Acum self.cb conține self.cb.tfidf_matrix
        self.train_df = train_df

    def explain(self, user_id, recommended_item_ids):
        explanations = {}
        # Get user's positive history
        user_history = self.train_df[
            (self.train_df['user_id'] == user_id) & (self.train_df['rating'] >= 4.0)
            ]
        if user_history.empty:
            for item_id in recommended_item_ids:
                rec_title = self.products.loc[item_id]['title']
                explanations[rec_title] = "Recommended based on general popularity (Collaborative Filtering)."
            return explanations
        user_item_indices = self.cb.item_id_to_idx[user_history['item_id']].values # Indicii item-urilor din istoric
        user_history_vectors = self.cb.tfidf_matrix[user_item_indices] # Vectorii TF-IDF ai item-urilor din istoric
        for item_id in recommended_item_ids:
            rec_title = self.products.loc[item_id]['title']
            item_idx = self.cb.item_id_to_idx[item_id] # Indexul item-ului recomandat
            item_vector = self.cb.tfidf_matrix[item_idx] # Vectorul TF-IDF al item-ului recomandat
            # Calculează similaritatea doar între acest item și istoricul userului
            sims_to_history = cosine_similarity(item_vector, user_history_vectors).ravel()
            # Găsește cel mai similar item din istoric
            most_similar_hist_idx_in_history = np.argmax(sims_to_history)
            most_similar_global_idx = user_item_indices[most_similar_hist_idx_in_history]
            most_similar_item_id = self.cb.idx_to_item_id[most_similar_global_idx]
            history_title = self.products.loc[most_similar_item_id]['title']
            msg = f"Recommended because it's similar to '{history_title}', which you liked."
            explanations[rec_title] = msg
        return explanations


def evaluate_model(model, train_df, test_df, k=10):
    print(f"\nEvaluating model with k={k}...")
    ground_truth_map = test_df[test_df['rating'] >= 4.0].groupby('user_id')['item_id'].apply(list).to_dict()
    test_users = list(ground_truth_map.keys())

    if not test_users:
        return {"error": "No test users with positive ratings to evaluate."}

    precisions, recalls, ndcgs = [], [], []

    for user_id in test_users:
        rec_items = model.recommend(user_id, train_df, k=k)

        gt_items = ground_truth_map.get(user_id, [])

        if not gt_items:
            continue  # User has no relevant items in test set, skip

        precisions.append(precision_at_k(rec_items, gt_items, k))
        recalls.append(recall_at_k(rec_items, gt_items, k))
        ndcgs.append(ndcg_at_k(rec_items, gt_items, k))

    metrics = {
        "precision_at_k": np.mean(precisions),
        "recall_at_k": np.mean(recalls),
        "ndcg_at_k": np.mean(ndcgs),
        "num_users_evaluated": len(precisions)
    }
    return metrics


def main():
    RATINGS_CSV = 'ratingsFinal.csv'
    PRODUCTS_CSV = 'productsFinal.csv'

    print("=== Loading and preprocessing dataset ===")
    try:
        train_data, val_data, test_data, products = load_and_preprocess(
            RATINGS_CSV,
            PRODUCTS_CSV,
            min_user_interactions=5,
            min_product_desc_len=20
        )
    except FileNotFoundError:
        print(f"Error: Could not find '{RATINGS_CSV}' or '{PRODUCTS_CSV}'.")
        print("Please update the file paths in main.py.")
        return

    print(f"Loaded {len(train_data)} train, {len(val_data)} val, {len(test_data)} test interactions.")
    print(f"Loaded {len(products)} products.")

    if test_data.empty:
        print("Test data is empty, cannot proceed with single-user test.")
        return

    test_user = test_data.iloc[0]["user_id"]
    print(f"\nSelected user for testing: {test_user}")

    # === Collaborative Filtering ===
    print("\n=== Collaborative Filtering Results ===")
    cf = CollaborativeFiltering(train_data)
    cf_recs = cf.recommend(test_user, train_data, k=10)
    print("CF Recommendations (item_ids):", cf_recs)
    save_model(cf, "cf_model")

    # === Content-Based Filtering ===
    print("\n=== Content-Based Filtering Results ===")
    cb = ContentBasedFiltering(products)
    cb_recs = cb.recommend_by_user_history(test_user, train_data, k=10)
    print("CB Recommendations (item_ids):", cb_recs)
    save_model(cb, "cb_model")

    # === Hybrid ===
    print("\n=== Hybrid Recommendation Results ===")
    hybrid_model = HybridRecommender(cf, cb, weight_cf=0.6, weight_cb=0.4)
    hybrid_recs = hybrid_model.recommend(test_user, train_data, k=10)
    print("Hybrid Recommendations (item_ids):", hybrid_recs)

    # === Eval Metrics ===
    print("\n=== Evaluation on Hybrid System ===")
    metrics = evaluate_model(hybrid_model, train_data, test_data, k=10)
    print(metrics)

    # === Explainability ===
    print("\n=== Explainability ===")
    xai = Explainability(products, cb, train_data)
    explanations = xai.explain(test_user, hybrid_recs)

    print("\nGenerated explanations:")
    for product_title, msg in explanations.items():
        print(f"➡ {product_title}: {msg}")


if __name__ == "__main__":
    main()