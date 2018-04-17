# JADE - JAva Driving Experience

Contributors : Amaury Zarzelli, Antoine Roca, Antoine Moutou, Augustin Gagnon, Eva Chen-Yen-Su, Laure Le Breton, Mariem Moukhlissi

## Prérequis
Oracle JDK version 8.
### Installation sur linux
Lancer les commandes suivantes pour installer le jdk oracle sur une distribution linux dérivée de Debian (comme Ubuntu par exemple)
```sh
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install oracle-java8-installer
```

## Téléchargement de JADE

Se placer dans un dossier, lancer la commande 
```sh
git clone https://gitlab.com/azarzelli/JADE.git
```

Une fois le téléchargement terminé lancer
```sh
cd JADE
git checkout demo
```
pour passer sur une branche stable de développement.

## Lancement du programme

Pour lancer le simulateur de conduite OpenDS, lancer la commande suivante dans un terminal ouvert dans le dossier racine de JADE :

```sh
java -jar JADE-0.0.1-SNAPSHOT.jar sim
```

Pour lancer la génération de scène OpenDS, utiliser la commande :
```sh
java -jar JADE-0.0.1-SNAPSHOT.jar build <NomDossier>
```
avec <NomDossier> le nom du sous-dossier du dossier "input" contenant des données exploitables par JADE (voir la partie ["Données en entrée"](#données-en-entrée))

## Données en entrée
Vous pouvez crééer vos propres scènes à partir de données issues du RGE de l'IGN, pour peu qu'elles correspondent parfaitment aux spécifications suivantes (des exemple de données en entrées sont présentes dans le dossier input) :
TODO