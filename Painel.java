import greenfoot.*;

/**
 * Um painel de texto desenhado na tela: usado para o placar (topo),
 * para o diário de bordo (base) e para o aviso de fim de jogo.
 */
public class Painel extends Actor
{
    private final int largura;
    private final int altura;
    private final Color fundo;

    public Painel(int largura, int altura, Color fundo)
    {
        this.largura = largura;
        this.altura = altura;
        this.fundo = fundo;
        mostrar();
    }

    /** Escreve as linhas, uma embaixo da outra, alinhadas à esquerda. */
    public void mostrar(String... linhas)
    {
        GreenfootImage img = fundoVazio();
        img.setFont(new Font("SansSerif", false, false, 14));
        img.setColor(Color.WHITE);
        for (int i = 0; i < linhas.length; i++) {
            img.drawString(linhas[i], 12, 20 + i * 18);
        }
        setImage(img);
    }

    /** Placar: texto à esquerda e barra de energia à direita. */
    public void mostrarPlacar(String texto, int energia)
    {
        GreenfootImage img = fundoVazio();
        img.setFont(new Font("SansSerif", true, false, 15));
        img.setColor(Color.WHITE);
        img.drawString(texto, 12, altura / 2 + 5);

        int barraLargura = 200;
        int barraX = largura - barraLargura - 15;
        int barraY = altura / 2 - 8;
        img.setColor(new Color(60, 60, 60));
        img.fillRect(barraX, barraY, barraLargura, 16);
        if (energia > 50) {
            img.setColor(new Color(60, 200, 90));
        } else if (energia > 20) {
            img.setColor(new Color(240, 190, 40));
        } else {
            img.setColor(new Color(230, 60, 50));
        }
        img.fillRect(barraX, barraY, barraLargura * energia / 100, 16);
        img.setColor(Color.WHITE);
        img.drawRect(barraX, barraY, barraLargura, 16);
        setImage(img);
    }

    /** Aviso grande e centralizado (fim de jogo / amanhecer). */
    public void mostrarAviso(String titulo, String detalhe, Color corTitulo)
    {
        GreenfootImage img = fundoVazio();
        GreenfootImage t = new GreenfootImage(titulo, 40, corTitulo, new Color(0, 0, 0, 0));
        GreenfootImage d = new GreenfootImage(detalhe, 18, Color.WHITE, new Color(0, 0, 0, 0));
        img.drawImage(t, (largura - t.getWidth()) / 2, altura / 2 - t.getHeight());
        img.drawImage(d, (largura - d.getWidth()) / 2, altura / 2 + 10);
        setImage(img);
    }

    private GreenfootImage fundoVazio()
    {
        GreenfootImage img = new GreenfootImage(largura, altura);
        img.setColor(fundo);
        img.fill();
        return img;
    }
}
