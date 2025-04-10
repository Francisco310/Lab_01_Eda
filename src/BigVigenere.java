import java.io.*;
import java.util.*;

public class BigVigenere{
    BufferedReader bR = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bW = new BufferedWriter(new OutputStreamWriter(System.out));

    private int[] key;
    private char[][] alfabeto;
    private static String abecedario = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    public BigVigenere() throws IOException {
        bW.write("Ingrese clave: ");
        bW.newLine();  // Asegura que se haga un salto de línea
        bW.flush();    // Envia el mensaje a la consola

        String clave = bR.readLine(); // Captura la clave ingresada
        boolean encontrado=true;

        this.key = new int[clave.length()];
        for (int i = 0; i < clave.length(); i++) {
            char letra = clave.charAt(i);
            int index = abecedario.indexOf(letra); // Encuentra la posición de la letra en el alfabeto
            if (index != -1) {
                this.key[i] = index;// Asigna el índice en el arreglo key
            }else{
                encontrado=false;
            }
        }

        if(encontrado!=true){
            bW.write( " Letra no encontrada");
            bW.newLine();
            bW.flush();
        }


        alfabeto = new char[abecedario.length()][abecedario.length()];
        for (int i = 0; i < abecedario.length(); i++) {
            for (int j = 0; j < abecedario.length(); j++) {
                alfabeto[i][j] = abecedario.charAt((i + j) % abecedario.length()); //convierte el string de abecedario a caracteres, usando el algoritmo de vigenere.
            }// esta parte que convierte es lo que te decia de que se suman (las i con las j)y se dividen por su longitud. (habia que saberselo)
        }
    }




    public BigVigenere(String numericKey)throws IOException{
        this.key = new int[numericKey.length()];
        boolean encontrar=true;
        for (int i = 0; i < numericKey.length(); i++) {
            char letra = numericKey.charAt(i);
            int index = abecedario.indexOf(letra);//Funcion que convierte string a clave numerica
            if(index!=-1){
                this.key[i]=index;
            }else{
                encontrar=false;
            }
        }

        if(encontrar!=true){
            bW.write(" Letra no encontrada");
            bW.newLine();
            bW.flush();
        }

        //Sirve para generar la matriz
        alfabeto = new char[abecedario.length()][abecedario.length()];
        for (int i = 0; i < abecedario.length(); i++) {
            for (int j = 0; j < abecedario.length(); j++) {
                alfabeto[i][j] = abecedario.charAt((i + j) % abecedario.length());
            }
        }
    }



    public String encrypt(String message) {
        StringBuilder encryptedMessage = new StringBuilder(); // Modifica las cadenas de caracteres
        int tamanomensaje = message.length();
        int contador=0;

        for (int i = 0; i < tamanomensaje; i++) {
            char letra = message.charAt(i);
            int x = abecedario.indexOf(letra);// Permite encontrar pos de caracter dentro de una cadena

            if( x!=-1){
                int claveindex = key[contador%key.length];//localiza la posicion de la letra a traves del indice.
                int cifrado = ((x+claveindex) % abecedario.length());//formula para cifrar
                encryptedMessage.append(abecedario.charAt(cifrado));
                contador++;
            }else{
                encryptedMessage.append(letra);
            }
        }
        return encryptedMessage.toString();
    }




    public String decrypt(String encryptedMessage){
        StringBuilder decryptedMessagge = new StringBuilder();//StringBuilder para asi poder modificar la cadena.
        int tamanomensaje = encryptedMessage.length();
        int contador=0;

        for(int i=0;i<tamanomensaje;i++){
            char letra = encryptedMessage.charAt(i);//convierte cada letra en un caracter.
            int x = abecedario.indexOf(letra); //busca la posicion de la letra en el string abecedario.

            if(x!=-1){
                int claveindex = key[contador%key.length]; //localiza la posicion de la letra a traves del indice.
                int descifrado = ((x-claveindex+abecedario.length())%abecedario.length());//formula para descifrar
                decryptedMessagge.append(abecedario.charAt(descifrado));
                contador ++;
            }else{
                decryptedMessagge.append(letra);
            }

        }
        return decryptedMessagge.toString();
    }


    public void reEncrypt() throws IOException{
        boolean encontrado=true;
        bW.write("Ingrese mensaje encriptado");
        bW.newLine();
        bW.flush();
        String nuevo = bR.readLine();
        String mensajeDescifrado = decrypt(nuevo);
        bW.write("Mensaje descencriptado: "+mensajeDescifrado);
        bW.newLine();
        bW.flush();

        bW.write("Ingrese nueva clave: ");
        bW.newLine();
        bW.flush();
        String nuevaClave = bR.readLine();
        this.key = new int[nuevaClave.length()];
        for (int i = 0; i < nuevaClave.length(); i++) {
            char letra = nuevaClave.charAt(i);
            int index = abecedario.indexOf(letra);//Funcion que convierte string a clave numerica
            if(index!=-1){
                this.key[i]=index;
            }else{
                encontrado=false;
            }
        }

        if(encontrado!=true){
            bW.write( " Letra no encontrada");
            bW.newLine();
            bW.flush();
        }

        String nuevoencrip=encrypt(mensajeDescifrado);
        bW.write("El nuevo mensaje encriptado es:"+nuevoencrip);
        bW.newLine();
        bW.flush();
    }

    public void evaluartiempos(String message) throws IOException{
        long inicio = System.nanoTime(); //variable para la medicion del tiempo en nanosegundos.
        String encriptacion=encrypt(message); // aqui se le manda a lo que le tomara el tiempo.
        long termino = System.nanoTime(); // termina de cronometrar el tiempo.
        long tiempo=termino-inicio;

        long inicio2 = System.nanoTime(); //aca se realiza la medicion de tiempo para desencriptar.
        String decriptacion=decrypt(encriptacion);
        long termino2 = System.nanoTime();
        long tiempo2=termino2-inicio2; //calculos de tiempo total que se demoro cada metodo.

        bW.write("Se demoro: "+tiempo+" nanosegundos en criptarse");
        bW.newLine();
        bW.write("Se demoro: "+tiempo2+" nanosegundos en decriptarse");
        bW.newLine();
        bW.flush();
    }


    public char search(int position)throws IOException{ // tratamos matriz como 1 dimension
        for (int i = 0; i < alfabeto.length; i++){ // Recorremos la matriz fila por fila
            for (int j = 0; j < alfabeto[i].length; j++) {
                if (position == i * alfabeto[i].length + j) {  // i = indice filas / j= indice de columnas
                    return alfabeto[i][j];
                }
            }
        }  return 'x';
    }



    public  char optimalSearch(int position)throws IOException{
        int filas = alfabeto.length; // 64
        int columnas= alfabeto[0].length; //64
        int totalposiciones= filas*columnas; // es para ver el total de posiciones que hay


        int x= position/columnas; // cuanta veces completamos una fila  //EJ: 130/64 = 2
        int y= position%columnas; // verifica cuantos pasos dentro de la fila // 130%64= 2
        // por lo tanto se encuentra en pos [2][2]; y de esa pos retorna la letra

        if(position>=totalposiciones || position<0){ //verificador de posicion.
            bW.write(" Posicion invalida");
            bW.newLine();
            bW.flush();
        }

        return alfabeto[x][y]; // x= 2 e y =2
    }









    public static void main(String[] args) throws IOException {

        BigVigenere bigVigenere = new BigVigenere();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        bw.write("Ingrese un mensaje para encriptar: ");
        bw.newLine();
        bw.flush();
        String mensaje = br.readLine();

        String mensajeEncriptado = bigVigenere.encrypt(mensaje);
        bw.write("Mensaje encriptado: " + mensajeEncriptado);
        bw.newLine();
        bw.flush();

        String mensajeDesencriptado = bigVigenere.decrypt(mensajeEncriptado);
        bw.write("Mensaje desencriptado: " + mensajeDesencriptado);
        bw.newLine();
        bw.flush();

        bigVigenere.evaluartiempos(mensaje);

        bw.write("Ingrese posicion buscar: ");
        bw.newLine();
        bw.flush();
        int posi = Integer.parseInt(br.readLine()); // Leer la posición y convertirla a número

        // Buscar el carácter correspondiente a la posición con la función menos eficiente
        char res = bigVigenere.search(posi);  // Llamada al método 'search' menos eficiente
        bw.write("El carácte en la posición " + posi + " es: " + res);
        bw.newLine();
        bw.flush();




        bw.write("Ingrese una posicion a buscar: ");
        bw.newLine();
        bw.flush();
        int pos = Integer.parseInt(br.readLine()); // Leer la posición y convertirla a número
        char resultado = bigVigenere.optimalSearch(pos); // Buscar el carácter correspondiente a la posición
        bw.write("El carácter en la posición " + pos + " es: " + resultado);
        bw.newLine();
        bw.flush();



    }

}