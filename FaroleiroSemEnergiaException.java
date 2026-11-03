/**
 * Exceção VERIFICADA lançada quando o Faroleiro tenta agir
 * (operar um recurso ou patrulhar) depois que sua energia chegou a 0.
 *
 * É o "fim de jogo de verdade": em vez de o farol voltar a ganhar
 * energia depois do "fim de jogo!", quem chamou fica sabendo que
 * a partida acabou.
 */
public class FaroleiroSemEnergiaException extends Exception
{
    public FaroleiroSemEnergiaException(String mensagem)
    {
        super(mensagem);
    }
}
