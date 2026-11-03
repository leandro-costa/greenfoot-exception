/**
 * Exceção VERIFICADA (checked) lançada quando um recurso do farol
 * é criado com um valor inválido (valor <= 0).
 *
 * Como estende Exception (e não RuntimeException), o compilador obriga
 * quem pode lançá-la a declarar "throws RecursoInvalidoException" e
 * quem chama a tratar com try/catch (ou repassar com throws).
 */
public class RecursoInvalidoException extends Exception
{
    public RecursoInvalidoException(String mensagem)
    {
        super(mensagem);
    }
}
