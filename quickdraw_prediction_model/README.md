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

## API Usage

Send POST request with image file:

```bash
curl -X POST -F "file=@your_image.png" https://zachttang-quickdraw.hf.space/predict
```