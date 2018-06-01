# hops
http://www.sa-robotics.com/it/hops/

HOPS Mission

Scopo finale del progetto è far rientrare una sonda metereologica al punto del lancio.

Iniziamo con il primo TASK:
far cadere la sonda da una altezza di 100 metri ed aprire il paracadute automaticamente quando la sonda si trova a 80 metri da terra.

Unico linguaggio di programmazione: JAVA


Overview
Scherzosamente abbiamo scelto come nome del progetto “HOPS” che significa “Hopefully opening parachute systems”, che in italiano è tradotto in “Siamo fiduciosi che il paracadute si apra”.

Come piattaforma hardware scegliamo Raspberry Pi equipaggiata con Raspbian Jessie Lite. Il software che girerà sulla piattaforma come già detto sarà sviluppato in java.

Hardware
Come hardware abbiamo scelto il BMP280 un sensore in grado di misurare la pressione atmosferica e la temperatura.

Collegamento

| BMP280 | Raspberry Pi Pin |
|--------|------------------|
| Vin    | Pin 2 (5V)       |
| GND    | Pin 6 (GND)      |
| SCK    | Pin 5 (I2C1_SCL) |
| SDI    | Pin 3 (I2C1_SDA) |

La numerazione dei pin della raspberry pi si basa sullo schema PI4J Raspberry Pi Zero (http://pi4j.com/pins/model-zero-rev1.html)

Dovete essere sicuri che i seguenti pin del BMP280 NON siano collegati: 3V0, SDO, CS
