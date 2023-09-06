import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import model.CustomColor;
import org.junit.Test;

/**
 * Represents a class that contains the tests for all methods in {@link CustomColor}.
 */
public class CustomColorTest {

  @Test
  public void testColorToString() {
    assertEquals("255 255 255", new CustomColor(Color.white).toString());
    assertEquals("0 0 0", new CustomColor(Color.black).toString());
    assertEquals("255 0 0", new CustomColor(Color.red).toString());
  }

  @Test
  public void testGetColors() {
    CustomColor orange = new CustomColor(Color.orange);
    assertEquals(255, orange.getRed());
    assertEquals(200, orange.getGreen());
    assertEquals(0, orange.getBlue());
  }

  @Test
  public void testEqualsAndHashCode() {
    assertTrue(new CustomColor(Color.red).equals(new CustomColor(255, 0, 0)));
    assertEquals(new CustomColor(255, 0, 0).hashCode(), new CustomColor(Color.red).hashCode());

    CustomColor c1 = new CustomColor(110, 200, 15);
    assertTrue(c1.equals(c1));

    assertFalse(c1.equals(new CustomColor(Color.orange)));

    assertFalse(c1.equals("Not a color"));
  }
}
