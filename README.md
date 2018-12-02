# TP4
***
   1909607 - Aminata Ndiaye

   1971729 - Gregoire Dervaux

   1973529 - Guillaume Kleinpoort

   1489757 - Patrick Nadeau
***
## Réseau
<a id="reseau"></a>
Comme nous allons devoir créer un flux de donnée entre différentes machines virtuelles, nous devons dans un premier temps créer un réseau privé virtuel.
Pour cela, nous allons utiliser VirtualBox ou VMware.
Pour cela, téléchargez VirtualBox [ici](https://www.virtualbox.org/wiki/Downloads), installez-le, puis lancez le logiciel.
Tout d'abord, nous allons créer un **réseau NAT**, d'adresse 192.168.111.0/24. (Fichier>>parametres>>reseau>>Creer/modifier)

![alt network configuration](img/reseau_configuration.png)

Ainsi, si nous utilisons ce réseau pour toutes les machines virtuelles, elles pourront communiquer sans avoir à utiliser de routeur intermédiaire.
Le réseau se décompose comme ceci:
+ l'**hôte** utilise le réseau quelconque auquel il est rattaché.
+ VirtualBox (ou VMware) jouera le rôle de **routeur** vers notre réseau virtuel
+ le **serveur TomCat** aura l'adresse **192.168.111.1**
+ le serveur de base de donnée **Cassandra** aura l'adresse **192.168.111.10**
+ la **grappe Spark** aura des adresses de la forme **192.168.111.2x**
    - *master*: *192.168.111.20*
    - *slave1*: *192.168.111.21*
    - *slave2*: *192.168.111.22*

Nous n'avons pas besoin de définir de port fowarding ici, il suffit de tout laisser passer.
Il peut être nécessaire de **désactiver le firewall** si il est actif sur les VM

***

## Configuration du Client Postman
Nous utilisons pour client le logiciel Postman, qui permet de créer rapidement des requêtes HTTP, en spécifiant des entêtes et des corps de requêtes customisée.
Pour faciliter son utilisation, nous avons utilisé une Collection de requête qui se trouve sur le dépôt Github.
Nous utilisons deux principales requêtes
+ Une requête **GET**:
    - sur les produits
    - sur les factures
    - sur un Id de facture spécifié en paramêtre d'URL.
+ Une requête **POST**

```javascript
{
  factureId: <factureId>
  product: [
      {
        productId: <productId>,
        prix: <productPrice>,
        qte: <productQuantity>
      },
      {...}]
}
```
Pour configurer le client, il vous faut simplement lancer PostMan sur la machine host, et importer la collection.
Une fois la collection chargée, il vous suffit de sélectionner l'onglet de la requête que vous voulez lancer, et cliquer sur *SEND*

> *PS:* Veuillez noter qu'il vous faudra modifier le corps de la requête POST si vous en faites plus d'une, car l'identifiant de la facture doit varier d'une facture à l'autre. Vous pouvez aussi modifier la quantité, le prix ou l'identifiant des produits tant que celui-ci existe

## Configuration des VM

Tout d'abord, il faut installer les Server sur les machines virtuelles.
Telechargez un iso de [linux-Server](https://www.ubuntu.com/download/server).
Créez une nouvelle machine virtuelle avec virtualBox:
+ nom: \<nom de la vm\>
+ type: linux
+ version Ubuntu
> Pour toutes les machines virtuelles utilisées ici, 1 à 2G de mémoire vive sera largement suffisant, car elle ne présentent pas d'interface graphique. Attention, comme  nous allons lancer 5 VM en même temps, veillez à ce que votre RAM ne satture pas. Si vous voulez donner 2G de RAM à vos machines virtuelles, vous devez disposer d'au moins 16G de RAM.

Nous devons maintenant configurer notre machine:
- Pour configurer l'OS:

  sélectionnez votre MV dans la page d'accueil >> Configuration >> stockage >> Contrôleur: IDE >> Choississez un disque>>selectionnez l'image disque telechargée apparavant

- Pour configurer le réseau:

  sélectionnez votre MV dans la page d'accueil >> Configuration >> Réseau >> Selectionnez le mode d'accès Réseau NAT >> Selectionnez le réseau "NatNetwork"

Lancez ensuite votre VM.

Sélectionnez la langue, le clavier, puis une fois dans l'interface de configuration réseau, sélectionnez votre interface réseau.
> Dans la plupart des cas, il s'agit de enp0s3


Puis Edit IPv4 >> DHCP methode >> Manual
Spécifiez les champs suivants:
+ Subnet 192.168.111.0/24
+ Adress: 192.168.111.xxx
+ Gateway: 192.168.111.255
> remplacez 192.168.111.xxx par l'adresse de la machine décrite [ici](#reseau)

Sélectionnez save, et continuez l'installation.

Une fois celle-ci terminée, redémarez la machine, identifiez-vous pour obtenir une invite de commande.

## Configuration du server Tomcat

Dans un premier temps, créez une nouvelle VM appelée server_web_tomcat à l'adresse **192.168.111.1**

Vérifiez qu'elle possède bien la bonne adresse ip en tapant dans l'invite de commande:
```bash
$ ifconfig
```
Installez ensuite le server TomCat:

```bash
#on met a jour la liste des packets
$ sudo apt-get update
# on installe la dernière version du kit de developpement java
$ sudo at-get install default-jdk
# on installe TomCat
$ sudo apt install tomcat8
# on demare le server tomcat:
$ sudo service tomcat8 start
```
