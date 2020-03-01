package Path;

/**
 * NoPathException is cast when there is no path
 * between two vertices.
 */

public class NoPathException extends RuntimeException
{
    public NoPathException(String errorMessage)
    {
        super(errorMessage);
    }


}
