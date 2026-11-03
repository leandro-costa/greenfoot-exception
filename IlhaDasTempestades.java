import greenfoot.*;
import java.util.ArrayList;
import java.util.List;

/**
 * O mundo: a Ilha das Tempestades durante a noite da tempestade.
 *
 * Objetivo: o Faroleiro (setas do teclado) precisa manter o farol aceso
 * até o amanhecer (PATRULHAS_ATE_AMANHECER patrulhas). Cada patrulha
 * gasta 10 de energia; Querosene e LenteDeCristal devolvem energia;
 * OndaGigante, o Contrabandista e o Espectro da Névoa atrapalham.
 *
 * A tempestade também traz recursos inválidos (valor <= 0). Eles são
 * recusados com RecursoInvalidoException e aparecem no diário de bordo.
 */
public class IlhaDasTempestades extends World
{
    public static final int LARGURA = 800;
    public static final int ALTURA = 560;
    public static final int ALTURA_PLACAR = 40;
    public static final int ALTURA_DIARIO = 120;

    // limites da área em que os habitantes e recursos circulam
    public static final int MAR_ESQUERDA = 15;
    public static final int MAR_DIREITA = LARGURA - 15;
    public static final int MAR_TOPO = ALTURA_PLACAR + 20;
    public static final int MAR_FUNDO = ALTURA - ALTURA_DIARIO - 20;

    private static final int PATRULHAS_ATE_AMANHECER = 20;
    private static final int INTERVALO_DA_TEMPESTADE = 70;
    private static final int LINHAS_NO_DIARIO = 6;

    private Faroleiro faroleiro;
    private Painel placar;
    private Painel diario;
    private final List<String> mensagens = new ArrayList<String>();
    private boolean encerrado = false;
    private int relogio = 0;
    private int recursosCriados = 0;

    public IlhaDasTempestades()
    {
        super(LARGURA, ALTURA, 1);
        IO.conectar(this);
        Greenfoot.setSpeed(50);
        setBackground(desenharCenario());
        setPaintOrder(Painel.class, Faroleiro.class, HabitanteDaIlha.class, ItemDaTempestade.class);
        prepare();
    }

    /**
     * Prepara a ilha. A criação do Faroleiro repete a linha 1 da revisão:
     * energia 120 é recusada pelo construtor, e QUEM CHAMOU (o mundo)
     * decide a correção, de forma visível no código.
     */
    private void prepare()
    {
        placar = new Painel(LARGURA, ALTURA_PLACAR, new Color(10, 20, 40, 220));
        addObject(placar, LARGURA / 2, ALTURA_PLACAR / 2);
        diario = new Painel(LARGURA, ALTURA_DIARIO, new Color(10, 20, 40, 220));
        addObject(diario, LARGURA / 2, ALTURA - ALTURA_DIARIO / 2);

        IO.println("A tempestade chegou à ilha. Use as setas para guiar o Faroleiro.");
        try {
            faroleiro = new Faroleiro("F1", 140, 260, 120, 0);
        } catch (IllegalArgumentException e) {
            IO.println("Habitante recusado: " + e.getMessage());
            IO.println("A ilha decide: F1 começa com energia 100.");
            faroleiro = new Faroleiro("F1", 140, 260, 100, 0);
        }
        addObject(faroleiro, 140, 260);
        addObject(new Contrabandista("C1", 620, 140, 80, "rum"), 620, 140);
        addObject(new EspectroDaNevoa("E1", 560, 360, 60, 1), 560, 360);
        atualizarPlacar();
    }

    @Override
    public void act()
    {
        if (encerrado) {
            return;
        }
        relogio++;
        if (relogio % INTERVALO_DA_TEMPESTADE == 0) {
            trazerRecurso();
        }
        atualizarPlacar();
        if (faroleiro.getPontuacao() >= PATRULHAS_ATE_AMANHECER) {
            encerrar("O farol resistiu à tempestade! Pontuação: " + faroleiro.getPontuacao(), true);
        }
    }

    /**
     * A tempestade traz um recurso com valor sorteado entre -5 e 25.
     * Se o valor for inválido, o construtor lança RecursoInvalidoException
     * e o recurso simplesmente não entra no mundo.
     */
    private void trazerRecurso()
    {
        recursosCriados++;
        int valor = Greenfoot.getRandomNumber(31) - 5;
        int sorteio = Greenfoot.getRandomNumber(100);
        int x = MAR_ESQUERDA + 20 + Greenfoot.getRandomNumber(MAR_DIREITA - MAR_ESQUERDA - 40);
        int y = MAR_TOPO + Greenfoot.getRandomNumber(MAR_FUNDO - MAR_TOPO);
        try {
            ItemDaTempestade item;
            if (sorteio < 40) {
                item = new Querosene("Q" + recursosCriados, valor);
            } else if (sorteio < 65) {
                item = new LenteDeCristal("L" + recursosCriados, valor);
            } else {
                item = new OndaGigante("O" + recursosCriados, valor);
                x = MAR_DIREITA - 10;
            }
            addObject(item, x, y);
        } catch (RecursoInvalidoException e) {
            IO.println("Recurso recusado: " + e.getMessage());
        }
    }

    /** Recebe as mensagens de IO.println e mostra no diário de bordo. */
    public void registrar(String mensagem)
    {
        mensagens.add(mensagem);
        while (mensagens.size() > LINHAS_NO_DIARIO) {
            mensagens.remove(0);
        }
        if (diario != null) {
            diario.mostrar(mensagens.toArray(new String[0]));
        }
    }

    /** Fim de jogo de verdade: congela a ilha e para a execução. */
    public void encerrar(String motivo, boolean vitoria)
    {
        if (encerrado) {
            return;
        }
        encerrado = true;
        atualizarPlacar();
        IO.println(motivo);
        Painel aviso = new Painel(560, 160, new Color(0, 0, 0, 200));
        if (vitoria) {
            aviso.mostrarAviso("AMANHECEU!", motivo, new Color(255, 220, 90));
        } else {
            aviso.mostrarAviso("FIM DE JOGO", motivo, new Color(240, 80, 70));
        }
        addObject(aviso, LARGURA / 2, (MAR_TOPO + MAR_FUNDO) / 2);
        Greenfoot.stop();
    }

    public boolean isEncerrado()
    {
        return encerrado;
    }

    /**
     * Clique com o botão direito no mundo e escolha este método para
     * rodar o roteiro de 11 linhas da revisão (classe Main). A saída
     * aparece no terminal do Greenfoot e no diário de bordo.
     */
    public void executarRoteiroDaRevisao()
    {
        Main.main(new String[0]);
    }

    private void atualizarPlacar()
    {
        String texto = faroleiro.getId()
            + "   energia: " + faroleiro.getEnergia()
            + "   patrulhas: " + faroleiro.getPontuacao() + "/" + PATRULHAS_ATE_AMANHECER;
        placar.mostrarPlacar(texto, faroleiro.getEnergia());
    }

    private static GreenfootImage desenharCenario()
    {
        GreenfootImage img = new GreenfootImage(LARGURA, ALTURA);
        // mar noturno em degradê
        for (int y = 0; y < ALTURA; y++) {
            int tom = 20 + y * 50 / ALTURA;
            img.setColor(new Color(10, tom, 40 + tom));
            img.drawLine(0, y, LARGURA, y);
        }
        // ilha
        img.setColor(new Color(90, 80, 60));
        img.fillOval(-120, 150, 420, 260);
        img.setColor(new Color(120, 110, 80));
        img.fillOval(-90, 175, 360, 210);
        // farol
        int fx = 60;
        for (int i = 0; i < 6; i++) {
            img.setColor(i % 2 == 0 ? new Color(220, 50, 50) : Color.WHITE);
            img.fillRect(fx, 140 + i * 20, 34, 20);
        }
        img.setColor(new Color(50, 50, 60));
        img.fillRect(fx - 4, 126, 42, 14);
        img.setColor(new Color(255, 240, 150));
        img.fillRect(fx + 6, 108, 22, 18);
        img.setColor(new Color(50, 50, 60));
        img.fillPolygon(new int[] {fx - 2, fx + 17, fx + 36}, new int[] {108, 90, 108}, 3);
        // feixe de luz
        img.setColor(new Color(255, 240, 150, 50));
        img.fillPolygon(new int[] {fx + 28, LARGURA, LARGURA}, new int[] {117, 40, 200}, 3);
        // ondas
        img.setColor(new Color(150, 190, 230, 120));
        for (int i = 0; i < 40; i++) {
            int x = Greenfoot.getRandomNumber(LARGURA);
            int y = ALTURA_PLACAR + Greenfoot.getRandomNumber(ALTURA - ALTURA_PLACAR - ALTURA_DIARIO);
            img.drawLine(x, y + 6, x + 10, y);
            img.drawLine(x + 10, y, x + 20, y + 6);
        }
        // chuva
        img.setColor(new Color(180, 200, 255, 90));
        for (int i = 0; i < 160; i++) {
            int x = Greenfoot.getRandomNumber(LARGURA);
            int y = Greenfoot.getRandomNumber(ALTURA);
            img.drawLine(x, y, x - 4, y + 12);
        }
        return img;
    }
}
