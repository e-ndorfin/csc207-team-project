# QuickDraw Prediction Model

## Preprocessing Requirements

The model expects 28x28 grayscale images. Preprocessing steps:

1. Convert image to grayscale
2. Resize to 28x28 pixels
3. Flatten to 784-element array (28×28)

## Drawing Application Settings

For optimal model predictions, 

**Canvas Size:** 28×28 pixels
**Brush Size:** 1-3 pixels 
**Colors:** Grayscale (0-255 range)
**Smoothing:** None or minimal

## Labels used in training
["airplane", "ambulance", "angel", "ant", "anvil", "apple", "arm", "asparagus", "axe", "backpack", "banana", "bandage", "barn", "baseball", "basket", "basketball", "bat", "bathtub", "beach", "bear", "beard", "bed", "bee", "belt", "bench", "bicycle", "binoculars", "bird", "blackberry", "blueberry", "book", "boomerang", "bottlecap", "bowtie", "bracelet", "brain", "bread", "bridge", "broccoli", "broom", "bucket", "bulldozer", "bus", "bush", "butterfly", "cactus", "cake", "calculator", "calendar", "camel"]


Easy: [""apple"", ""arm"", ""axe"", ""banana"", ""bed"", ""bee"", ""belt"", ""book"", ""bread"", ""broom"", ""bucket"", ""bush"", ""cactus"", ""cake"", ""calendar""]
Medium: [""airplane"", ""ant"", ""anvil"", ""backpack"", ""bandage"", ""barn"", ""baseball"", ""basket"", ""basketball"", ""bat"", ""bathtub"", ""bench"", ""bicycle"", ""bird"", ""blackberry"", ""blueberry"", ""bottlecap"", ""bowtie"", ""brain"", ""broccoli"", ""bus"", ""butterfly"", ""calculator""]
Hard: [""ambulance"", ""angel"", ""asparagus"", ""beach"", ""bear"", ""beard"", ""binoculars"", ""boomerang"", ""bracelet"", ""bridge"", ""bulldozer"", ""camel""]

## API Usage

Send POST request with image file:

```bash
curl -X POST -F "file=@your_image.png" https://zachttang-quickdraw.hf.space/predict
```
