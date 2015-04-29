package trendli.me.makhana.client;

public class Flavor
{
    // Single colors

    // Water
    public static final Flavor BLUE_RASPBERRY = new Flavor( 0, 0 );
    // Dirt
    public static final Flavor CHOCOLATE = new Flavor( 1, 0 );
    // Grass
    public static final Flavor LIME = new Flavor( 2, 0 );
    // Sand
    public static final Flavor LEMON = new Flavor( 3, 0 );
    // Sidewalk
    public static final Flavor ROCK_CANDY = new Flavor( 4, 0 );
    // Road
    public static final Flavor LICORICE = new Flavor( 5, 0 );

    // Light Leaves
    public static final Flavor WINTERGREEN = new Flavor( 0, 1 );
    // Dark Leaves
    public static final Flavor MINT = new Flavor( 1, 1 );
    // Dark Bark
    public static final Flavor DARK_ROAST = new Flavor( 2, 1 );
    // Light Bark
    public static final Flavor LIGHT_ROAST = new Flavor( 3, 1 );

    // Windows
    public static final Flavor BLUE_BUBBLEGUM = new Flavor( 0, 2 );
    // White Walls
    public static final Flavor WHIPPED_CREAM = new Flavor( 1, 2 );
    // Red Walls
    public static final Flavor CHERRY = new Flavor( 2, 2 );
    // Brown Walls
    public static final Flavor CARAMEL = new Flavor( 3, 2 );
    // Tan Walls
    public static final Flavor CREAM_SODA = new Flavor( 4, 2 );
    // Blue Walls
    public static final Flavor BLUEBERRY = new Flavor( 5, 2 );

    // Pairs

    // Dirt & Sand
    public static final Flavor LEMON_CHOCOLATE = new Flavor( 0, 8 );
    // Grass & Sand
    public static final Flavor LEMON_LIME = new Flavor( 1, 8 );

    // Light Leaves & Dark Bark
    public static final Flavor WINTERGREEN_DARK_ROAST = new Flavor( 0, 9 );
    // Light Leaves & Light Bark
    public static final Flavor WINTERGREEN_LIGHT_ROAST = new Flavor( 1, 9 );
    // Dark leaves & Dark Bark
    public static final Flavor MINT_DARK_ROAST = new Flavor( 2, 9 );
    // Dark leaves & Light Bark
    public static final Flavor MINT_LIGHT_ROAST = new Flavor( 3, 9 );

    private final int offsetX;
    private final int offsetY;

    private Flavor( int offsetX, int offsetY )
    {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * @return the offsetX
     */
    public int getOffsetX( )
    {
        return offsetX;
    }

    /**
     * @return the offsetY
     */
    public int getOffsetY( )
    {
        return offsetY;
    }
}
