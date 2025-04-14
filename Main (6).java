public class Main implements IEstruturaSimples {
    private int capacidade;
    private Object[] elementos;
    private int inicio;
    private int fim;
    private int tamanho;


    public Main(int capacidadeInicial) {
        this.capacidade = capacidadeInicial;
        this.elementos = new Object[capacidade];
        this.inicio = 0;
        this.fim = -1;
        this.tamanho = 0;
    }

    public Main() {
        this(10);
    }

    @Override
    public void inserirElemento(Object elemento) {
        if (estaCheia()) {
            dobrarCapacidade();
        }
        fim = (fim + 1) % capacidade;
        elementos[fim] = elemento;
        tamanho++;
    }

    @Override
    public void inserirElementoIndice(Object elemento, int indice) {
        if (indice < 0 || indice > tamanho) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }

        if (estaCheia()) {
            dobrarCapacidade();
        }

        if (indice == tamanho) {
            inserirElemento(elemento);
            return;
        }

        for (int i = tamanho; i > indice; i--) {
            elementos[(inicio + i) % capacidade] = elementos[(inicio + i - 1) % capacidade];
        }

        elementos[(inicio + indice) % capacidade] = elemento;
        fim = (fim + 1) % capacidade;
        tamanho++;
    }

    @Override
    public void inserirSequencia(Object elementos) {
        if (elementos instanceof Object[]) {
            Object[] array = (Object[]) elementos;
            for (Object elemento : array) {
                inserirElemento(elemento);
            }
        } else {
            throw new IllegalArgumentException("Deve ser passado um array de objetos");
        }
    }

    @Override
    public void removerElemento() {
        if (estaVazia()) {
            return;
        }
        elementos[inicio] = null;
        inicio = (inicio + 1) % capacidade;
        tamanho--;
    }

    @Override
    public Object removerIndice(int indice) {
        if (indice < 0 || indice >= tamanho) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }

        int posicaoReal = (inicio + indice) % capacidade;
        Object removido = elementos[posicaoReal];

        for (int i = indice; i < tamanho - 1; i++) {
            int atual = (inicio + i) % capacidade;
            int proximo = (inicio + i + 1) % capacidade;
            elementos[atual] = elementos[proximo];
        }

        elementos[fim] = null;
        fim = (fim - 1 + capacidade) % capacidade;
        tamanho--;

        return removido;
    }

    @Override
    public void removerSequencia(Object elementos) {
        if (elementos instanceof Object[]) {
            Object[] array = (Object[]) elementos;
            for (Object elemento : array) {
                removerTodasOcorrencias(elemento);
            }
        } else {
            throw new IllegalArgumentException("Deve ser passado um array de objetos");
        }
    }

    @Override
    public void removerTodasOcorrencias(Object elemento) {
        int writeIndex = inicio;
        int count = 0;

        for (int i = 0; i < tamanho; i++) {
            int currentIndex = (inicio + i) % capacidade;
            if (elementos[currentIndex] != null && elementos[currentIndex].equals(elemento)) {
                continue;
            }
            elementos[writeIndex % capacidade] = elementos[currentIndex];
            writeIndex++;
            count++;
        }

        for (int i = count; i < tamanho; i++) {
            elementos[(inicio + i) % capacidade] = null;
        }

        tamanho = count;
        fim = (inicio + count - 1) % capacidade;
    }

    @Override
    public boolean estaCheia() {
        return tamanho == capacidade;
    }

    @Override
    public boolean estaVazia() {
        return tamanho == 0;
    }

    @Override
    public boolean buscarElemento(Object elemento) {
        for (int i = 0; i < tamanho; i++) {
            int index = (inicio + i) % capacidade;
            if (elementos[index] != null && elementos[index].equals(elemento)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object buscarElementoIndice(int indice) {
        if (indice < 0 || indice >= tamanho) {
            throw new IndexOutOfBoundsException("Índice inválido: " + indice);
        }
        return elementos[(inicio + indice) % capacidade];
    }

    @Override
    public void ordenarCrescente() {
        for (int i = 0; i < tamanho - 1; i++) {
            for (int j = 0; j < tamanho - i - 1; j++) {
                int index1 = (inicio + j) % capacidade;
                int index2 = (inicio + j + 1) % capacidade;

                if (elementos[index1] instanceof Comparable && elementos[index2] instanceof Comparable) {
                    Comparable c1 = (Comparable) elementos[index1];
                    Comparable c2 = (Comparable) elementos[index2];

                    if (c1.compareTo(c2) > 0) {
                        Object temp = elementos[index1];
                        elementos[index1] = elementos[index2];
                        elementos[index2] = temp;
                    }
                }
            }
        }
    }

    @Override
    public void ordenarDecrescente() {
        for (int i = 0; i < tamanho - 1; i++) {
            for (int j = 0; j < tamanho - i - 1; j++) {
                int index1 = (inicio + j) % capacidade;
                int index2 = (inicio + j + 1) % capacidade;

                if (elementos[index1] instanceof Comparable && elementos[index2] instanceof Comparable) {
                    Comparable c1 = (Comparable) elementos[index1];
                    Comparable c2 = (Comparable) elementos[index2];

                    if (c1.compareTo(c2) < 0) {
                        Object temp = elementos[index1];
                        elementos[index1] = elementos[index2];
                        elementos[index2] = temp;
                    }
                }
            }
        }
    }

    @Override
    public int quantidadeElementos() {
        return tamanho;
    }

    @Override
    public void dobrarCapacidade() {
        Object[] novoArray = new Object[capacidade * 2];
        for (int i = 0; i < tamanho; i++) {
            novoArray[i] = elementos[(inicio + i) % capacidade];
        }
        elementos = novoArray;
        capacidade *= 2;
        inicio = 0;
        fim = tamanho - 1;
    }

    @Override
    public void editarElemento(Object elementoAntigo, Object elementoNovo) {
        for (int i = 0; i < tamanho; i++) {
            int index = (inicio + i) % capacidade;
            if (elementos[index] != null && elementos[index].equals(elementoAntigo)) {
                elementos[index] = elementoNovo;
            }
        }
    }

    @Override
    public void limpar() {
        elementos = new Object[capacidade];
        inicio = 0;
        fim = -1;
        tamanho = 0;
    }

    @Override
    public void exibir() {
        System.out.print("Fila: [");
        for (int i = 0; i < tamanho; i++) {
            int index = (inicio + i) % capacidade;
            System.out.print(elementos[index]);
            if (i < tamanho - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    @Override
    public Object obterPrimeiroElemento() {
        return estaVazia() ? null : elementos[inicio];
    }

    @Override
    public Object obterUltimoElemento() {
        return estaVazia() ? null : elementos[fim];
    }

    public static void main(String[] args) {
        Main fila = new Main(3);

        System.out.println("=== TESTE DE INSERÇÃO ===");
        fila.inserirElemento(10);
        fila.inserirElemento(20);
        fila.inserirElemento(30);
        fila.exibir();

        System.out.println("\n=== TESTE DE REMOÇÃO ===");
        fila.removerElemento();
        fila.exibir();

        System.out.println("\nTESTE DE REDIMENSIONAMENTO");
        fila.inserirElemento(40);
        fila.inserirElemento(50); // Deve dobrar capacidade aqui
        fila.exibir();
        System.out.println("Capacidade atual: " + fila.capacidade);

        System.out.println("\nORDENAÇÃO ");
        fila.ordenarCrescente();
        fila.exibir();
    }
}