(function() {
  "use strict";
  window.onload = function() {
    var images = document.querySelectorAll(".logo-container img");
    function adjustImageWidth(image) {
      var widthBase   = 50;
      var scaleFactor = 0.525;
      var imageRatio  = image.naturalWidth / image.naturalHeight;
      image.width = Math.pow(imageRatio, scaleFactor) * widthBase;
    }
    images.forEach(adjustImageWidth);
  };
}());
