import numpy as np

def precision_at_k(recommended_items, ground_truth_items, k):
    recs = recommended_items[:k]
    return len(set(recs) & set(ground_truth_items)) / k

def recall_at_k(recommended_items, ground_truth_items, k):
    recs = recommended_items[:k]
    return len(set(recs) & set(ground_truth_items)) / len(ground_truth_items) if ground_truth_items else 0.0

def dcg_at_k(recommended_items, ground_truth_items, k):
    dcg = 0.0
    for i, item in enumerate(recommended_items[:k]):
        rel_i = 1.0 if item in ground_truth_items else 0.0
        denom = np.log2(i+2)
        dcg += rel_i / denom
    return dcg

def idcg_at_k(ground_truth_items, k):
    # ideal DCG: all relevant in top positions
    ideal_rels = [1.0] * min(len(ground_truth_items), k)
    idcg = sum([rel / np.log2(i+2) for i, rel in enumerate(ideal_rels)])
    return idcg

def ndcg_at_k(recommended_items, ground_truth_items, k):
    idcg = idcg_at_k(ground_truth_items, k)
    if idcg == 0: return 0.0
    return dcg_at_k(recommended_items, ground_truth_items, k) / idcg
