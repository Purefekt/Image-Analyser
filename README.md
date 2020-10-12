# Image Analyser

## Google Play store download link
[DOWNLOAD LINK](https://play.google.com/store/apps/details?id=unideb.hu.veersingh.mobilesolutions)

## Project idea
I wanted to make a simple app which detects basic elements in an image
selected by the user

## Approach
- I went with Microsoft Azure computer vision API for my application. I went with
the free version as it was a hobby project and I did not have so much money to
spend on the paid tiers. The free plan gives me 5000 requests per month and 20
requests per minute.
- I had to write an android app which would ask the user to pick an image from
the gallery and then press an “Analyse” button. When the analyse button was
pressed the image data would be converted to a bitmap and that data would be
sent to Azure computers.
- Then the app would get data about the image in json format, which I then
implemented in a TextView.
