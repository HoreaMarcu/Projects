import pandas as pd
from sklearn.model_selection import train_test_split

def load_and_preprocess(ratings_csv, products_csv, min_user_interactions=5, min_product_desc_len=20):
    r = pd.read_csv(ratings_csv)
    p = pd.read_csv(products_csv)

    user_counts = r['user_id'].value_counts()
    active_users = user_counts[user_counts >= min_user_interactions].index
    r = r[r['user_id'].isin(active_users)]

    p['desc_len'] = p['description'].fillna("").apply(len)
    p = p[p['desc_len'] >= min_product_desc_len]

    r = r[r['item_id'].isin(p['item_id'].unique())]

    train, test = train_test_split(r, test_size=0.2, random_state=42, stratify=r['user_id'])
    train, val = train_test_split(train, test_size=0.125, random_state=42)  # approx 70/10/20

    return train.reset_index(drop=True), val.reset_index(drop=True), test.reset_index(drop=True), p.reset_index(drop=True)
