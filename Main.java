/**
 * O roteiro de 11 linhas da revisão "Onde isso quebra?", agora com
 * tratamento de exceções. Mesmos valores (120, 15, -4, 70, null...).
 *
 * No Greenfoot: clique com o botão direito na classe Main e escolha
 * "void main(String[] args)", ou clique com o botão direito no mundo e
 * escolha "executarRoteiroDaRevisao()". A saída aparece no terminal.
 *
 * Regras seguidas:
 * - cada linha que pode falhar está em um try/catch específico
 *   (nada de catch (Exception e) genérico);
 * - uma falha não impede as linhas seguintes; a linha 11 sempre aparece;
 * - o finally mostra a energia de F1 depois de cada operação.
 */
public class Main
{
    public static void main(String[] args)
    {
        Faroleiro f;
        try {
            f = new Faroleiro("F1", 0, 0, 120, 0);                       // linha 1
        } catch (IllegalArgumentException e) {
            IO.println("Habitante recusado: " + e.getMessage());
            IO.println("Main decide: F1 será criado com energia 100.");
            f = new Faroleiro("F1", 0, 0, 100, 0);
        }

        RecursoDoFarol q = null;
        try {
            q = new Querosene("Q1", 15);                                 // linha 2
        } catch (RecursoInvalidoException e) {
            IO.println("Recurso recusado: " + e.getMessage());
        }

        RecursoDoFarol l = null;
        try {
            l = new LenteDeCristal("L1", -4);                            // linha 3
        } catch (RecursoInvalidoException e) {
            IO.println("Recurso recusado: " + e.getMessage());
        }

        RecursoDoFarol o = null;
        try {
            o = new OndaGigante("O1", 70);                               // linha 4
        } catch (RecursoInvalidoException e) {
            IO.println("Recurso recusado: " + e.getMessage());
        }

        try {
            f.patrulhar();                                               // linha 5
        } catch (FaroleiroSemEnergiaException e) {
            IO.println(e.getMessage());
        } finally {
            IO.println(f.getId() + " está com energia " + f.getEnergia());
        }

        operar(f, o);                                                    // linha 6
        operar(f, l);                                                    // linha 7
        operar(f, null);                                                 // linha 8
        operar(f, o);                                                    // linha 9
        operar(f, q);                                                    // linha 10

        IO.println(f.getId() + " termina com energia " + f.getEnergia()); // linha 11
    }

    /** Linhas 6 a 10: operarRecurso protegido por try/catch/finally. */
    private static void operar(Faroleiro f, RecursoDoFarol recurso)
    {
        try {
            f.operarRecurso(recurso);
        } catch (IllegalArgumentException e) {
            IO.println("Operação recusada: " + e.getMessage());
        } catch (FaroleiroSemEnergiaException e) {
            IO.println(e.getMessage());
        } finally {
            IO.println(f.getId() + " está com energia " + f.getEnergia());
        }
    }
}
