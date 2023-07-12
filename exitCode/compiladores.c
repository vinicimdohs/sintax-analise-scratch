#include <stdio.h>

int main(){ 

	int TempoEmAnos, ValorSalario;
	scanf("%d %d ", &TempoEmAnos, &ValorSalario);
	printf("%d ", TempoEmAnos);

	if(TempoEmAnos>10) {

	ValorSalario=100 ;
	}
	else{

	ValorSalario=ValorSalario*2 ;

	}
	printf("%d ", ValorSalario);

	return 0;
}

