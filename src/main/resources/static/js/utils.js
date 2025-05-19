document.addEventListener("DOMContentLoaded", function () {
	
	const dialog = document.getElementById('customDialog');
	const openBtn = document.querySelector('.open-dialog-btn');
	const closeBtn = dialog?.querySelector('.close-btn');

	if (dialog && openBtn && closeBtn) {
		function openDialog() {
			dialog.classList.add('active');
		}
		
		function closeDialog() {
			dialog.classList.remove('active');
		}
		
		openBtn.addEventListener('click', openDialog);
		closeBtn.addEventListener('click', closeDialog);
		
		dialog.addEventListener('click', function (e) {
			if (e.target === dialog) {
				closeDialog();
			}
		});
	} else {
		console.warn('ダイアログの要素が取得できませんでした。');
	}
		  
	const searchButton = document.querySelector('.search-button');
	if (searchButton) {
		searchButton.addEventListener('click', function (event) {
			const keywordInput = document.querySelector('.keyword');
	        if (keywordInput && keywordInput.value.trim() === "") {
				event.preventDefault();
				alert("商品名を入力してください");
			}
		});
	}
	
	const deleteForms = document.querySelectorAll(".delete-form");

	deleteForms.forEach(function (form) {
		form.addEventListener("submit", function (e) {
			const productName = form.dataset.name || "この商品";
	        const confirmed = confirm(`「${productName}」 を本当に削除しますか？`);
			if (!confirmed) {
				e.preventDefault(); // キャンセルされた場合は送信させない
			}
		});
	});
		
    const dropZone = document.querySelector('.image-upload');
    const fileInput = document.querySelector('#imageFile');

    if (!dropZone || !fileInput) return;

    const preview = document.createElement('div');
    preview.style.marginTop = '10px';
	
	const existingImage = dropZone.dataset.existingImage;
	if (existingImage) {
		// 既存画像があれば、タイムスタンプを追加してキャッシュ防止
		const timestampedImage = `${existingImage}?t=${new Date().getTime()}`;
	    preview.innerHTML = `<img src="${timestampedImage}" alt="既存画像" style="max-height:150px; margin-top:10px;">`;
	}

    dropZone.appendChild(preview);

    function handleFile(file) {
        if (!file.type.startsWith('image/')) {
            alert('画像ファイル（.jpg, .jpeg, .png）を選択してください。');
            return;
        }

        const reader = new FileReader();
        reader.onload = function (e) {
            preview.innerHTML = `<img src="${e.target.result}" alt="プレビュー" style="max-height:150px; margin-top:10px;">`;
        };
        reader.readAsDataURL(file);
    }

    dropZone.addEventListener('dragover', (e) => {
        e.preventDefault();
        dropZone.style.backgroundColor = '#e6f7ff';
    });

    dropZone.addEventListener('dragleave', () => {
        dropZone.style.backgroundColor = '#fafafa';
    });

    dropZone.addEventListener('drop', (e) => {
        e.preventDefault();
        dropZone.style.backgroundColor = '#fafafa';

        const files = e.dataTransfer.files;
        if (files.length > 0) {
            fileInput.files = files;
            handleFile(files[0]);
        }
    });

    fileInput.addEventListener('change', () => {
        if (fileInput.files.length > 0) {
            handleFile(fileInput.files[0]);
        }
    });
	  
});
