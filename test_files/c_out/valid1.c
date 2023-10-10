#include <stdio.h>
#include <string.h>
#include <math.h>
char string_0[256];
char string_1[256];
char string_2[256];
char string_3[256];
char string_4[256];
char string_5[256];
char string_6[256];
char string_7[256];
char string_8[256];
char string_9[256];
char string_10[256];
char string_11[256];
char string_12[256];
char string_13[256];
char string_14[256];
char string_15[256];
char string_16[256];
char string_17[256];
char string_18[256];
char string_19[256];
char string_20[256];
char string_21[256];
char string_22[256];
char string_23[256];
char string_24[256];
char string_25[256];
char string_26[256];
char string_27[256];
char* str_concat(char *s1, char *s2, char *dest) {
    char _s1[256];
    char _s2[256];
    strcpy(_s1, s1);
    strcpy(_s2, s2);
    strcat(_s1, _s2);
    strcpy(dest, _s1);
    return dest;
}

char* int_to_string(int a, char *dest) {
    sprintf(dest, "%d", a);
    return dest;
}

char* real_to_string(float a, char *dest) {
    sprintf(dest, "%f", a);
    return dest;
}

char* bool_to_string(int a) {
    if(a) return "true";
    return "false";
}int id_0=1;
float id_1(int id_2, float id_3, char* id_4) {
float id_5;
id_5=((id_2 + id_3) + id_0);
if((id_5>100)) {
strcpy(string_0,"grande");
strcpy(id_4, string_0);
}else {
strcpy(string_1,"piccola");
strcpy(id_4, string_1);
}return id_5;
}
void id_6(char* id_7) {
int id_8=1;
while((id_8<=4)) {
int id_9=1;
printf("%s\r\n","");
id_8=(id_8 + id_9);

}
printf("%s\r\n",id_7);
}
int main() {
int id_2=1;
float id_3=2.2;
strcpy(string_3,"no");
float id_10=id_1(id_2,id_3,&string_2);
id_6(str_concat(str_concat(str_concat(str_concat(str_concat(str_concat(str_concat("la somma di ",int_to_string(id_2,string_4),string_5)," e ",string_6),real_to_string(id_3,string_7),string_8)," incrementata di ",string_9),int_to_string(id_0,string_10),string_11)," e\" ",string_12),string_2,string_13));
id_6(str_concat("ed è pari a ",real_to_string(id_10,string_14),string_15));
printf("%s\t","vuoi continuare? (si/no)");
scanf("%s",string_3);
while((strcmp(string_3, "si") == 0)) {
printf("%s","inserisci un intero:");
scanf("%d",&id_2);
printf("%s","inserisci un reale:");
scanf("%f",&id_3);
id_10=id_1(id_2,id_3,&string_2);
id_6(str_concat(str_concat(str_concat(str_concat(str_concat(str_concat(str_concat("la somma di ",int_to_string(id_2,string_16),string_17)," e ",string_18),real_to_string(id_3,string_19),string_20)," incrementata di ",string_21),int_to_string(id_0,string_22),string_23)," e\" ",string_24),string_2,string_25));
id_6(str_concat(" ed è pari a ",real_to_string(id_10,string_26),string_27));
printf("%s","vuoi continuare? (si/no):\t");
scanf("%s",string_3);

}
printf("%s\r\n","");
printf("%s","ciao");

return 0;
}