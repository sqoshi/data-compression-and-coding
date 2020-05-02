### Obrazy w formacie TGA
####Wstęp
Program oblicza wyniki entropii dla
całego kodu i poszczególnych składowych 
wykorzystując różnicę między predykatorami JPEG-LS.
W skład wchodzi 7 starych i 1 nowy standard:
1.  "W"
2.  "N"
3.  "NW"
4.  "N + W - NW"
5.  "N + (W - NW)/2"
6.  "W + (N - NW)/2"
7.  "(N + W)/2"
8.  "
    [ min(N,W), if NW>=max(A,B)
    
    [ max(N,W), if NW<=min(A,B)
    
    [ N + W - NW", else"
Wszystkie symbole są sprawdzane metodą control
działanie mod 256 i jeśli liczba jest ujemna to +256.
Wtedy wszystkie symbole bitmap stają się dodatnimi liczbami( intami).

Program był kompilowany na java 13/14, ubuntu 20.04.

####Algorytm 
1. Wczytujemy zawartość zdjęcia formatu .tga pomiędzy 18 a file.length-26.
2. Z 13,14 i 15,16 bajtu pobieramy szerokosc i wysokosc obrazku.
3. Wczytujemy jako int i kontrolujemy ich wartość metodą control mod256 and if c<0 c+256.
4. Tworzymy na podstawie danych 3D tablicę lub inaczej 2D tablicę z elementem [B,G,R], gdzie b,g,r są skłądowymi kolorami.
5. Obliczamy entropię dla pliku wejściowego i składowych.
6. Tworzymy tablicę różnić 
    1. Bierzemy istniejący pixel z mother_board
    2. Wyznaczamy predykcję zgodnie z konkretnym schematem
    2. odejmujemy od X predykcję.
7. Obliczamy entropie całościową i składowe dla tak stworzonej tablicy różnić.
    


####Uruchomienie

~~~
javac Driver.java 
java Driver args
~~~
np.:
~~~
java Driver example0.tga example1.tga example2.tga example3.tga
~~~
lub
~~~
java Driver obraz.tga
~~~