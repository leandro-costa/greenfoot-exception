/**
 * Saída do jogo no mesmo estilo usado nas aulas (IO.println).
 *
 * Tudo o que é impresso vai para o terminal do Greenfoot
 * (System.out) e, se houver uma ilha ativa, também aparece
 * no diário de bordo exibido na parte de baixo do mundo.
 */
public class IO
{
    private static IlhaDasTempestades ilha;

    /** Chamado pelo mundo quando ele é criado (ou reiniciado). */
    public static void conectar(IlhaDasTempestades novaIlha)
    {
        ilha = novaIlha;
    }

    public static void println(Object mensagem)
    {
        String texto = String.valueOf(mensagem);
        System.out.println(texto);
        if (ilha != null) {
            ilha.registrar(texto);
        }
    }
}
