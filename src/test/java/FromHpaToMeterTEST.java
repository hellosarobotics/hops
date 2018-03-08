import com.sarobotics.utils.FromHpaToMeter;
import org.junit.Test;
import static org.junit.Assert.*;

public class FromHpaToMeterTEST {

  @Test
  public void fromHpaToMeter(){
    double altitudeInMeter = FromHpaToMeter.fromHpaToMeter(943.452060619145,68.43763649081811);
    assertEquals(613.3638226338744,altitudeInMeter, 0);
  }
}
