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

## Details about the application
#### HomeActivity
This is the splash screen and shows the logo of my app and the
version code for 2 seconds before moving on to the MainActivity.
#### MainActivity
The MainActivity consists of a TextView which tells the user some
basic information. Followed by a Button which lets the user to move on to the 
Veer Singh CR97JH
Page2 and a TextView at the bottom telling the user to shake the phone. I have
implemented a shake detector in my application, on shaking the device it starts
the AboutPage where the user can see some details about the developer(me).
#### AboutPage
The about Page consists of some basic information about the dev,
an image of the dev and an image of the university logo where I am studying.
#### Page2
This activity contains all the primary functions of the app. Here there is
an ImageView with a blank image placeholder. The user can press the “Choose
Image” button to pick an image from the gallery which appears in place of the
placeholder. That is followed by a TextView which guides the user to pressing
the “Analyse” button. The Analyse button will not work if no image is selected. If
an image is selected the analyse button converts the image to a bitmap and
sends the data to azure cloud, from where it receives data about the image in
json format. Then that data is converted to a string and is shown in place in the
TextView. In case of any ambiguity or errors there is a “Not Working?” button
which takes the user to NotWorking activity.
#### NotWorking
This activity has a scrollview with a linear layout inside it. It
contains many TextView in a vertical linear order with some errors the user
might encounter.
#### Miscellaneous
- The app uses internet and external storage permissions.
- All pages are locked to portrait mode as they malfunction in landscape
mode.
- If the API is unable to detect the image, then it gives a toast message
saying, “Sorry I can’t understand this image… Blame – Veer Singh!”
- The MainActivity has an exit warning which prompts the user to press
back twice in under 2 seconds to successfully exit the application.
- The apps colors are in line with University of Debrecen (my university)
colors - FFAD05 and 004532 (Hex).
- The logo was designed by my friend Juanita.

## Hierarchy

-------------------------------------

## Building the application
- I started with making an app which simply analysis a hard-coded image from the
drawable folder, connects to the Azure API and shows the result. I called this
“Image Analyser”. [Repo](https://github.com/Purefekt/Image-Analyser-hard-coded-image)
- Then I made another app which lets the user pick an image from the gallery and
then displays the image using the image’s URI. I called this “Pick Image from
Gallery”. [Repo](https://github.com/Purefekt/Pick-image-from-gallery)
- Then I made an app which detects shakes. All it does it opens a new activity
whenever the phone is shaken. I called this “Shake Detector”. [Repo](https://github.com/Purefekt/Shake-detector)
- Finally, I tried to merge the Image Analyser and the Pick Image from Gallery into
a test app which I simply called “testt”. I had many issues in this version but I was
finally able to debug it and in the end I implemented the shake detection into
the testt app, added some more basic activities like “NotWorking” activity and
“AboutPage” activity and my app was ready.

## Issues faces
My testt app kept crashing any time I chose an image and ran the
analyser. The image analyser kept failing and crashing because it had a null pointer
exception. At this point my image analyser was running inside onCreate method and my
pick image from gallery method was outside onCreate. The pick image from gallery
method was also only getting the image URI. So I placed the image analyser outside
onCreate and inside pick image from gallery method, I then created a variable bmp
which got the bitmap of the chosen image instead of the URI and then I passed that
bitmap to the image analyser and it finally worked.
