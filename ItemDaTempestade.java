import greenfoot.*;

/**
 * O molde dos recursos que a tempestade traz pelo mar.
 *
 * Mesma ideia da classe Decapoda do tutorial: o que é comum a Querosene,
 * LenteDeCristal e OndaGigante (id, valor, validação, tempo no mar)
 * fica aqui; cada filha só diz o seu nome, a sua imagem e como usar().
 *
 * Mudança do Desafio 20: setValor não troca mais um valor inválido por 1.
 * Ele lança RecursoInvalidoException. Por ser uma exceção verificada,
 * setValor e os construtores declaram throws RecursoInvalidoException.
 */
public abstract class ItemDaTempestade extends Actor implements RecursoDoFarol
{
    private static final int TEMPO_NO_MAR = 420;

    private String id;
    private int valor;
    private int tempoRestante = TEMPO_NO_MAR;

    public ItemDaTempestade(String id, int valor) throws RecursoInvalidoException
    {
        this.id = id;
        setValor(valor);
    }

    private void setValor(int valor) throws RecursoInvalidoException
    {
        if (valor <= 0) {
            throw new RecursoInvalidoException("Valor inválido para o recurso " + id + ": " + valor);
        }
        this.valor = valor;
    }

    /** Valor guardado, disponível só para as filhas calcularem usar(). */
    protected int getValor()
    {
        return valor;
    }

    /** Nome legível para o diário de bordo, ex.: "Querosene Q3". */
    public String getNome()
    {
        return getClass().getSimpleName() + " " + id;
    }

    /** Recursos não ficam para sempre no mar: somem depois de um tempo. */
    @Override
    public void act()
    {
        IlhaDasTempestades ilha = (IlhaDasTempestades) getWorld();
        if (ilha == null || ilha.isEncerrado()) {
            return;
        }
        tempoRestante--;
        if (tempoRestante < 90) {
            getImage().setTransparency(Math.max(0, tempoRestante * 255 / 90));
        }
        if (tempoRestante <= 0) {
            ilha.removeObject(this);
        }
    }
}
