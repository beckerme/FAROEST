# FAROEST

## Banco do FaroEST

A EST, através da sua associada ESTretenimento, aposta forte no mercado de entretenimento e 
está prestes a lançar um remake do famoso jogo do Spectrum – West Bank - que se deve juntar à 
sua extensa lista de sucessos, que inclui: ConquEST, ESTrada, Bloons, Zombie ESTerminator, etc.  
O Banco do FaroEST é um jogo em que o jogador veste a pele do segurança de um banco. Nesta 
posição terá de eliminar os bandidos que tentam assaltar o banco, acalmar os clientes que estejam 
insatisfeitos e permitir que os clientes depositem dinheiro no banco. O banco tem 9 portas pelas 
quais os visitantes podem entrar. A cada momento o jogador controla 3 portas. Para passar de nível 
tem de assegurar que houve, pelo menos, um depósito em cada uma das 9 portas.

## Controlos

As teclas usadas no jogo estão descritas na tabela seguinte: 

![image](https://github.com/beckerme/FAROEST/assets/75760847/2dfb2cce-81a6-4470-8a56-5bfab5ce0a1b)

Só se pode disparar para uma porta de cada vez. 
Só se pode mudar de portas quando todas estiverem fechadas. 

# Mecânica do jogo 
O jogo está dividido em níveis e o objetivo e ir passando de nível até terminar todos. Para passar 
de nível todas as portas do banco terão de receber dinheiro. Para isso deverá entrar um cliente que 
deposite dinheiro em cada porta. A passagem de nível dá-se quando todas as portas se fecharem e 
todas tiverem depósitos. Em cada momento do jogo estão visíveis 3 das 9 portas que constituem as 
entradas do banco. Para garantir depósitos em todas, o jogador terá de ir mudando de portas 
visíveis.  

O jogador começa com 3 vidas. Por cada ação correta o jogado recebe pontos e, por cada ação 
errada, o jogador perde uma vida. Quando não tiver vidas, o jogador perde o jogo. 
Além de mudar de portas, o jogador pode disparar para as portas visíveis. Como só tem uma 
pistola só pode disparar para uma porta de cada vez. 
A cada nível que passa o jogo torna-se mais complicado, pois os personagens aumentam em 
número e em rapidez e também mudam de comportamento.  
Personagens 

O jogo tem várias personagens e cada uma pode assumir um dado comportamento sempre que 
visita o banco. Há personagens que numa visita são depositantes e noutra são assaltantes. Neste 
momento a lista de personagens é a seguinte:

![image](https://github.com/beckerme/FAROEST/assets/75760847/b7e4b36f-3922-4f13-91ba-82384ace092b)

**NOTA: Esta lista de personagens pode ser alterada no futuro.**

# Comportamento das Visitas

Cada visitante tem um determinado comportamento, o qual afeta a pontuação, modo de interação, 
animações usadas, etc. Neste momento, há 5 comportamentos diferentes. Cada visitante tem uma 
série de estados em que pode realizar operações diferentes ou reagir de maneira diferente. 

## Depositante 

Este visitante representa um personagem que vai depositar dinheiro no banco. Enquanto a porta 
se abre, este visitante saúda o jogador. Assim que a porta abre, o depositante fica no estado espera. 
Após um tempo aleatório a porta fecha-se, o depositante diz adeus e o dinheiro é depositado. Além 
do depósito, o visitante atribui também uma pontuação quando a porta fecha. 
Um visitante pode ter associado a si extras. Por exemplo, o mineiro tem como extras uma picareta 
e um cachimbo. Estes extras fornecem mais pontuação, desde que o jogador os retire ao depositante, 
disparando sobre eles. Mesmo que tenha extras quando a porta fecha, o depósito é realizado. 
Se o jogador disparar sobre o depositante em qualquer estado diminui um dos extras que este tem. 
Se disparar quando este não tem extras, o depositante morre e o jogador perde uma vida. 

## Assaltante 

Representa um personagem que vai assaltar o banco. O jogador deve eliminar estes personagens, 
disparando sobre eles. Se o jogador não eliminar o assaltante, este acaba por disparar sobre o 
jogador, fazendo-o perder uma vida. O assaltante pode ter as armas empunhadas ou ter as armas no 
coldre (está em espera). Se vier com as armas no coldre, após um tempo aleatório saca-as e depois 
passa ao estado sacou. O assaltante dispara após um tempo aleatório depois de sacar as armas.  
O jogador perde uma vida se disparar sobre o assaltante antes deste sacar as armas.  
A porta fecha quando o assaltante é eliminado. Não há depósito neste caso. 
O jogador recebe uma pontuação por eliminar o assaltante, com base no tempo de reação (ver 
tabela seguinte) após este sacar as armas (ou a porta abrir caso já venha com as armas empunhadas). 

![image](https://github.com/beckerme/FAROEST/assets/75760847/49ed96ca-b2bd-42a5-a9ec-0e814b35598a)
Os assaltantes não têm extras. 

## Troca

Neste caso há dois personagens: um personagem que vai depositar o dinheiro, mas que entretanto 
(tempo aleatório) é interrompido por outro que representa um assaltante já com as armas 
empunhadas. O jogador deve eliminar o assaltante. Se o jogador disparar antes de o assaltante 
aparecer, perde uma vida. Após um tempo aleatório depois de aparecer, o assaltante dispara e o 
jogador perde uma vida. Se eliminar o assaltante recebe uma pontuação de acordo com o tempo de 
reação após o aparecimento do assaltante e de acordo com a tabela do visitante assaltante. 
A porta fecha após o assaltante ser eliminado. Neste caso não há depósito. 
Neste visitante não há extras.  

## Aleatório 

Este visitante tem sempre extras. Estes extras devem ser todos removidos. O último extra a 
remover revela se o visitante vai depositar ou fazer estragos. Se for para depositar, o jogador deve 
disparar de novo sobre o visitante. Se não for para depositar e o jogador disparar, perde uma vida. 
Por exemplo, no peregrino é preciso remover todos os chapéus e o último revela se o peregrino tem 
uma bomba ou um saco de dinheiro. Se for o saco de dinheiro, um disparo provoca o depósito, 
recebe a pontuação base e a porta fecha. Se for a bomba, um disparo faz o jogador perder uma vida. 
Após algum tempo a porta fecha. Se ainda tem extras, não há depósito nem estragos. Também 
não se recebe qualquer pontuação. Por cada extra removido recebe-se a pontuação base. 

## Insatisfeito 

Este representa um cliente insatisfeito que vem “ajustar contas” com o banco trazendo um objeto 
que poderá explodir. Cabe ao jogador pacificá-lo através de uma troca calma de argumentos (leia-se 
disparando sobre ele e tirando-lhe o objeto explosivo). Este visitante pode ter mais extras. Neste 
caso terá de remover todos os extras para poder remover o explosivo. 
Ao fim de um tempo aleatório a porta fecha. Se o objeto explosivo não foi removido, rebenta e o 
jogador perde uma vida. Se o objeto explosivo foi removido e o visitante pacificado, tem-se direito 
a depósito e à respetiva pontuação. 

Se o jogador dispara após ter removido o objeto explosivo, perde uma vida. 
Por cada extra retirado (incluindo o explosivo) o jogador recebe a pontuação base. 
Cada personagem pode assumir um qualquer comportamento em cada visita. A tabela seguinte 
mostra os comportamentos que cada personagem pode ter (esta tabela pode mudar no futuro!). 

![image](https://github.com/beckerme/FAROEST/assets/75760847/27a438f7-915d-4849-bcfa-ef5ba5588960)

# Níveis  

Como mencionado cada nível tem tipos de visitantes, pontuações e tempos de reação diferentes. 
Uma maneira de garantir isso é criar, para cada nível, uma lista de visitantes possíveis, com as 
configurações pretendidas. Quando for necessário criar um visitante, basta clonar um da lista, 
escolhido aleatoriamente. 

# O que já está feito e o que falta fazer 

A primeira versão do sistema já está implementada, com suporte apenas para o comportamento 
de depositar e assaltante. Em algumas partes do código estão comentários TODO que indicam 
coisas que devem implementadas, melhoradas ou que alertam para coisas mal feitas. Durante a 
evolução do jogo poderão alterar as partes do código que acharem pertinente, exceto as do package 
prof.jogos2D. Podem criar e eliminar classes, alterar classes, etc. 

A pontuação está feita, mas falta a parte de ler e gravar as pontuações máximas. Assim devem 
completar a classe HighScoreHandler. O ficheiro a ler/gravar é  “highscores.bfe”. O formato do 
ficheiro é simples: são sempre 20 linhas em que cada linha tem uma pontuação máxima. 

A pontuação tem um nome (ocupando 20 caracteres no máximo) e uma pontuação (máximo de 7 
dígitos). Se um nome não ocupar os 20 caracteres estes devem ser completados com ‘.’ (ponto) no 
final. A seguir ao nome deve haver sempre um ‘.’ (ponto). Antes da pontuação deve haver sempre 
um ‘$’ (cifrão). Se a pontuação não ocupar os 7 dígitos deve ser completada por ‘$’ antes. 
Além do ficheiro “highscores.bfe” é também fornecido o ficheiro “highscores_base.bfe” para 
poderem recuperar o ficheiro caso este tenha sido corrompido por tentativas de gravação falhadas. 
