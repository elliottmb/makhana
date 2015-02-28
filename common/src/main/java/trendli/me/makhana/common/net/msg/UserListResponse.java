package trendli.me.makhana.common.net.msg;

import java.util.ArrayList;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

@Serializable
public class UserListResponse extends AbstractMessage
{

    private ArrayList< String > accountNames;

    public UserListResponse( )
    {
    }

    public UserListResponse( ArrayList< String > accountNames )
    {
        this.accountNames = accountNames;
    }

    public ArrayList< String > getAccountNames( )
    {
        return accountNames;
    }

    public void setAccountNames( ArrayList< String > accountNames )
    {
        this.accountNames = accountNames;
    }

}
