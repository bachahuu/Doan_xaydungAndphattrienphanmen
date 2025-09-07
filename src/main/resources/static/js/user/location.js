document.addEventListener("DOMContentLoaded", function () {
    const checkbox = document.getElementById("ckbstore");
    const storeinfomation = document.getElementById("storeInfomation");
    const infomation_an = document.getElementById("info_an");
    const infomation_an1 = document.getElementById("info_an1");
    const infomation_an2 = document.getElementById("info_an2");
  
    //sử lý sự kiện
    checkbox.addEventListener("change", function () {
      if (checkbox.checked) {
        storeinfomation.style.display = "flex";
        infomation_an.style.display = "none";
        infomation_an1.style.display = "none";
        infomation_an2.style.display = "none";
      } else {
        storeinfomation.style.display = "none";
        infomation_an.style.display = "flex";
        infomation_an1.style.display = "flex";
        infomation_an2.style.display = "flex";
      }
    });
  });
  