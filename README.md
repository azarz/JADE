# JADE - JAva Driving Experience

Contributeurs : Amaury Zarzelli, Antoine Roca, Antoine Moutou, Augustin Gagnon, Eva Chen-Yen-Su, Laure Le Breton, Mariem Moukhlissi

Présentation : https://docs.google.com/presentation/d/10xvvla_IB1JZLCCMmGqoEnwjLShZt0B-8iQdBtzx1ik/

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
git clone https://github.com/azarz/JADE.git
```

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

Pour lancer la génération puis le simulateur directement après le calcul des données :
```sh
java -jar JADE-0.0.1-SNAPSHOT.jar both <NomDossier>
```

## Données en entrée
Vous pouvez crééer vos propres scènes à partir de données issues du RGE de l'IGN, pour peu qu'elles correspondent parfaitment aux spécifications suivantes :

+ La zone totale à traiter (MNT, orthoimage, shapefiles) doit être un carré, avec un côté d'au maximum 3km², voire 2km² en fonction de la puissance à disposition.

+ Le MNT doit ête contenu dans un seul fichier de type Arc/Info Ascii grid (asc), nommé DTM_1m.asc. Il doit préférablement avoir la ligne concernant la NODATA_value (entre la cellsize et le début du tableau de valeurs) (spécifications sur : https://mesange.educagri.fr/htdocs/sigea/supports/QGIS/distance/perfectionnement/M09_Traitement_donnees_raster_gen_web/co/10_N2_Ouverture_MNT.html)

+ L'orthoimage doit être au format PNG et son emprise doit correspondre à celle du MNT. Elle doit avoir comme nom ortho.png

+ Les couches vecteurs ne doivent avoir des données que sur une zone restreinte, correspondant à l'emprise du MNT. Les fichier à avoir sont les suivants (ils doivent tous exister, même s'ils ne contiennent aucune géométrie):
	+ BATI_INDIFFERENCIE.DBF, BATI_INDIFFERENCIE.SHP, BATI_INDIFFERENCIE.SHX
	+ ROUTE.DBF, ROUTE.SHP, ROUTE.SHX
	+ SURFACE_EAU.DBF, SURFACE_EAU.SHP, SURFACE_EAU.SHX
	+ ZONE_VEGETATION.DBF, ZONE_VEGETATION.SHP, ZONE_VEGETATION.SHX

	+ (les dénominations correspondent à celles de la BD TOPO)


Tous ces fichiers doivent être dans un sous-dossier du dossier input, dont le nom doit être donné en argument lors du lancement du programme.
Exemple : 
```sh
java -jar JADE-0.0.1-SNAPSHOT.jar build Nation
```
