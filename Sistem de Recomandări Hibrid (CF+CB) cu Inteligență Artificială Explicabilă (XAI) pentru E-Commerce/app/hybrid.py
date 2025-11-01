def weighted_hybrid_score(user_id, item_id, cf_score, cb_score, w_cf=0.6, w_cb=0.4):
    return w_cf * cf_score + w_cb * cb_score

def recommend_weighted(user_id, candidate_items, cf_model_predict_fn, cb_scores, w_cf=0.6, w_cb=0.4, top_k=10):
    scores = []
    for item in candidate_items:
        cf_s = cf_model_predict_fn(user_id, item)
        cb_s = cb_scores.get(item, 0.0)
        s = weighted_hybrid_score(user_id, item, cf_s, cb_s, w_cf, w_cb)
        scores.append((item, s, cf_s, cb_s))
    scores.sort(key=lambda x: -x[1])
    return scores[:top_k]
