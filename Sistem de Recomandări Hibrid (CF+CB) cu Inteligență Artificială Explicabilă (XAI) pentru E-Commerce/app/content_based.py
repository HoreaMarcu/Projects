import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity

def build_tfidf_embeddings(product_df, text_col='description', max_features=20000):
    tfidf = TfidfVectorizer(max_features=max_features, stop_words='english')
    tfidf_matrix = tfidf.fit_transform(product_df[text_col].fillna(""))
    return tfidf, tfidf_matrix

def build_bert_embeddings(product_df, model_name='all-MiniLM-L6-v2', text_col='description', batch_size=64):
    embedder = SentenceTransformer(model_name)
    texts = product_df[text_col].fillna("").tolist()
    embeddings = embedder.encode(texts, batch_size=batch_size, show_progress_bar=True, convert_to_numpy=True)
    return embedder, embeddings

def item_similarity_tfidf(item_idx, tfidf_matrix, top_k=10):
    vec = tfidf_matrix[item_idx]
    sims = cosine_similarity(vec, tfidf_matrix).ravel()
    top = np.argsort(-sims)[1:top_k+1]
    return top, sims[top]
