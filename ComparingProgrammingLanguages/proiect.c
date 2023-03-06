#include <stdlib.h>
#include <stdio.h>


int main(int argc, char** argv) {
	int a[102];
	int *b = (int*)malloc(102*sizeof(int));
	int aux;
	int steps = 0;
	for(int i = 0; i < 100; i++){
		a[i] = rand() % 100;
		steps++;
	}
	for(int i = 0; i < 100; i++){
		for(int j = i + 1; j < 100; j++){
			if(a[i] > a[j]){
				steps += 3;
				aux = a[i];
				a[i] = a[j];
				a[j] = aux;
			}
		}
	}
	int steps2 = 0;
	for(int i = 0; i < 100; i++){
		b[i] = rand() % 100;
		steps2++;
	}
	for(int i = 0; i < 100; i++){
		for(int j = i + 1; j < 100; j++){
			if(b[i] > b[j]){
				steps2 += 3;
				aux = b[i];
				b[i] = b[j];
				b[j] = aux;
			}
		}
	}
	printf("Static array sorted in %d steps.\n", steps);
	printf("Static array sorted in %d steps.\n", steps2);
	return 0;
}
