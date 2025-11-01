import pandas as pd
import sys

# --- Configuration ---
INPUT_FILE = 'AMAZON_FASHION.json'
RATINGS_OUTPUT = 'ratingsFinal.csv'
PRODUCTS_OUTPUT = 'productsFinal.csv'
# ---------------------

print(f"Loading {INPUT_FILE}...")
print("This may take a moment...")

try:
    df = pd.read_json(INPUT_FILE, lines=True)
except FileNotFoundError:
    print(f"Error: '{INPUT_FILE}' not found.")
    print("Please make sure the file is in the same directory as this script.")
    sys.exit(1)
except Exception as e:
    print(f"Error loading JSON file: {e}")
    print("The file might be corrupted or not in the expected JSON-lines format.")
    sys.exit(1)

print(f"Loaded {len(df)} total reviews.")

print(f"Creating {RATINGS_OUTPUT}...")

ratings_df = df[['reviewerID', 'asin', 'overall', 'unixReviewTime']].copy()

ratings_df.rename(columns={
    'reviewerID': 'user_id',
    'asin': 'item_id',
    'overall': 'rating',
    'unixReviewTime': 'timestamp'
}, inplace=True)

ratings_df.to_csv(RATINGS_OUTPUT, index=False)
print(f"Successfully created {RATINGS_OUTPUT}.")

print(f"Creating {PRODUCTS_OUTPUT}...")

df['summary'] = df['summary'].fillna('')
df['reviewText'] = df['reviewText'].fillna('')

df['description_content'] = df['summary'] + ' ' + df['reviewText']

products_df = df.groupby('asin').agg(
    description=('description_content', lambda s: ' '.join(s.astype(str)))
).reset_index()

products_df['category'] = 'Fashion'

products_df.rename(columns={'asin': 'item_id'}, inplace=True)


products_df['title'] = products_df['item_id']

products_df = products_df[['item_id', 'title', 'description', 'category']]

products_df.to_csv(PRODUCTS_OUTPUT, index=False)
print(f"Successfully created {PRODUCTS_OUTPUT} with {len(products_df)} unique products.")

print("\nConversion complete! You can now run main.py.")