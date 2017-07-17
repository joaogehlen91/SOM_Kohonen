# Self Organizing Map (SOM) Kohonen
Implementação de Mapas Auto-Organizáveis de Kohonen para reconhecimento de caracteres

Alunos: Elias Fank; João Gehlen; Ricardo Zanuzzo


## Como compilar e executar:
### Por Makefile: 
 ```sh
  $ make all
  $ make run <dim_neuronio> <max_epocas> <taxa_ap> <raio> <test_size>
```

Onde \<dim_neuronio> é o tamanho da dimensão do mapa de neurônios (Exemplo 15 = 15x15).

\<max_epocas> representa o número de treitamentos.

\<taxa_ap> taxa de aprendizado inicial do treinameto.

\<raio> tamanho do raio inicial usado no calculo da vizinhança (Exemplo 0.7 = 70%).

E \<test_size> percentual dos valores de entrada que serão usados para o teste (Exemplo 0.2 = 20%).

### Exemplo de execução:
```sh
  $ make run 10 20 0.1 0.5 0.3
```
Nessa execução vai gerar matriz de neurônios de 10x10, com 20 treinamentos, taxa de aprendizagem do treinamento de 0.1, com raio inicial de 50% da matriz de neurônios e 30%  da entrada usada para validação.

### Exemplo de execução padrão:
```sh
  $ make run
```
Nessa execução o programa vai utilizar valores padrões.
