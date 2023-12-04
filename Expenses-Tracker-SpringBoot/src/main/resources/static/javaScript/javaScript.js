// This function will triger when user tries to leave the page or navigate to another page

    onbeforeunload = function() {
	   document.getElementById("formTag").reset();
    }
    function openImage()
    {
        document.getElementById("eyeImg").style.transition = "all 0.54s"
        document.getElementById("inpPass").type= 'text';
        document.getElementById("eyeImg").src="/Images/eye close.png"
        document.getElementById("eyeImg").style.transform =  "rotateY(180deg)"
        
    }
    function closeImage()
    {
        document.getElementById("eyeImg").style.transition = "all 0.54s"
        document.getElementById("inpPass").type= 'password';
        document.getElementById("eyeImg").src="/Images/eye.png"
        document.getElementById("eyeImg").style.transform =  "rotateY(0deg)"
    }
    
    function borderOn(idOfTag)
    {
        document.getElementById(idOfTag).style.border= "2px solid navy"
    }
    function borderOFF(idOfTag)
    {
        document.getElementById(idOfTag).style.border= "none"
    }
    function stopBack()
    {
		history.go(1)
	}
	
	const fileInput = document.getElementById("imageInput");
	const displayImage = document.getElementById("displayImage");
	
	fileInput.addEventListener("change", function(){
		const file = fileInput.files[0];
		if(file && file.type.startsWith("image/"))
		{
			const fileReader = new FileReader();
			fileReader.onload = function(e) {
				displayImage.src = e.target.result;
				
			};
			fileReader.readAsDataURL(file)
		}
	});
	

	
	document.getElementById("editSymbol").addEventListener("click", function()
    {
		imageInput.click();
	})