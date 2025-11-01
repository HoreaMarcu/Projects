from implicit.als import AlternatingLeastSquares
from scipy.sparse import coo_matrix
from surprise import Dataset, Reader, SVD, KNNBasic

def train_als(train_df, factors=50, regularization=0.01, iterations=20):
    user_map = {uid: i for i, uid in enumerate(train_df['user_id'].unique())}
    item_map = {iid: i for i, iid in enumerate(train_df['item_id'].unique())}

    rows = train_df['user_id'].map(user_map).to_numpy()
    cols = train_df['item_id'].map(item_map).to_numpy()
    data = train_df['rating'].to_numpy().astype(float)

    sparse = coo_matrix((data, (rows, cols)), shape=(len(user_map), len(item_map)))
    model = AlternatingLeastSquares(factors=factors, regularization=regularization, iterations=iterations)
    model.fit((sparse.T).astype('double'))

    return model, user_map, item_map, sparse

def train_svd_surprise(train_df):
    reader = Reader(rating_scale=(train_df['rating'].min(), train_df['rating'].max()))
    data = Dataset.load_from_df(train_df[['user_id','item_id','rating']], reader)
    trainset = data.build_full_trainset()
    svd = SVD(n_factors=50, biased=True)
    svd.fit(trainset)
    return svd
