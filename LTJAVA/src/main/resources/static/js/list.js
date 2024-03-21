//khi trang web load xong cac thanh phaanf cow bản
//Thì sẽ gọi hàm ready của thành phần này
$(document).ready(function() {
    $.ajax({
    //1.Thành phần url đường dẫn API
        url: 'http://localhost:8080/api/v1/books',
        //2. Phương thức gọi API: GET POST DELETE UPDATE
        type: 'GET',
        //3. LOại data trả về nhận là JSON, XML, PLAINTEXT
        dataType: 'json',
        success: function(data) {
            let trHTML = '';
            //vòng lặp đê lấy từng quyển sách và phát sinh chuỗi tr, td trong table tương ứng
            $.each(data, function(i, item) {
                trHTML += '<tr id="book-' + item.id + '">' +
                    '<td>' + item.id + '</td>' +
                    '<td>' + item.title + '</td>' +
                    '<td>' + item.author + '</td>' +
                    '<td>' + item.price + '</td>' +
                    '<td>' + item.categoryName + '</td>' +
                    //kiểm tra quyển sách đ show action tương ứng
                    '<td sec:authorize="hasAnyAuthority(\'ADMIN\')">' +
                    '<a href="/books/edit/' + item.id + '" class="text-primary">Edit</a>| ' +
                    '<a href="/books/' + item.id + '" class="text-danger" onclick="apiDeleteBook(' + item.id + ', this); return false;">Delete</a>' +
                    '</td>' +
                    '</tr>';
            });
            //gán chuỗi strHTML vaof id book table để hiển thị
            $('#book-table-body').append(trHTML);
        }
    });
});

//định nghĩa javasript function apiDeleteBook để gọi khi user bam nut delete
function apiDeleteBook(id) {
//hien thi papup de confirm vs nguoi dung
    if (confirm('Are you sure you want to delete this book?')) {
    //tien hanh goi ajax de call api delete book
        $.ajax({
        //1.dinh nghia url duong dan api
            url: 'http://localhost:8080/api/v1/books/' + id,
            //2.dinh nghiaphuong thuc goi la GET DELTE POST UPDATE
            type: 'DELETE',
            //4.khi goi thanh cong thi show alert thong bao
            success: function() {
                alert('Book deleted successfully!');
                //5.dong thoi remove voi id Book id tuong ung
                $('#book-' + id).remove();
            }
        });
    }
}


