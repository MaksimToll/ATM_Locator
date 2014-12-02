var SUCCESS_MESSAGE = "<strong>Success!</strong> <span>Your data is saved successfully!</span>";
var INFO_MESSAGE = "<strong>Info!</strong> <span>Nothing to update!</span>";
var ERROR_MESSAGE = "<strong>Error!</strong> <span>Error saving data!</span>";
var WARNING_MESSAGE = "<strong>Warning!</strong> <span>See the validation rules!</span>";

function hidePopover(element) {
    $('#' + element).popover("destroy");
}
function hidePopoveDelay(element) {
    setTimeout(function () {
        $('#' + element).popover("destroy");
    }, 2000);
};

function getUser() {
    var user = {
        id: $("#id").val(),
        login: $("#login").val(),
        email: $("#email").val(),
        password: $("#password").val(),
        confirmPassword: $("#confirmPassword").val(),
        avatar: ($("#image")[0].files[0] != undefined) ? $("#image")[0].files[0] : null
    };
    return user;
}
function validateForm(persistedUser) {
    var result = true;
    var user = getUser();
    if (user.login == persistedUser.login &&
        user.email == persistedUser.email &&
        user.password.length == 0 &&
        user.confirmPassword == 0 &&
        user.avatar == persistedUser.avatar) {
        showAlert("alert alert-info", INFO_MESSAGE);
        return false;
    }
    if (user.login != persistedUser.login && !validateLogin(user.login)) {
        $('#login').attr("data-content", "Login is too short(min 4 letters) or has unsupported character");
        $('#login').popover("show");
        result = false;
    }
    if (user.email != persistedUser.email && !validateEmail(user.email)) {
        $('#email').attr("data-content", "Email isn't valid (example: someone@domain.com)");
        $('#email').popover("show");
        result = false;
    }
    if (user.password.length != 0 && !validatePasswordStrange(user.password)) {
        $('#password').attr("data-content", "Password is invalid. Password must have minimum 6 characters, uppercase letter, lowercase letter and digit");
        $('#password').popover("show");
        result = false;
    }
    if (!validateConfirmPassword(user.password, user.confirmPassword)) {
        $('#confirmPassword').attr("data-content", "Password and confirm password are different");
        $('#confirmPassword').popover("show");
        result = false;
    }
    if (!result) {
        showAlert("alert alert-warning", WARNING_MESSAGE);
    }
    return result;
}

function showAlert(className, html) {
    var element = $("div.alert");
    element.removeClass();
    element.addClass(className);
    element.children(".close").nextAll().remove();
    element.append(html);
    element.show();
}

$(document).ready(function () {
    var persistedUser = getUser();

    function changeImage(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                $('#userAvatar').attr('src', e.target.result);
            }
            reader.readAsDataURL(input.files[0]);
        }
    }

    $("#image").change(function () {
        changeImage(this);
    });

    $("#save").click(function () {

            if (validateForm(persistedUser)) {
                var user = getUser();
                var fd = new FormData();
                fd.append("id", user.id);
                fd.append("login", user.login);
                fd.append("email", user.email);
                fd.append("confirmPassword", user.confirmPassword);
                fd.append("avatar", user.avatar);
               //check if password is changed
                if (user.password.length != 0){
                    fd.append("password", user.password);
                }
                $.ajax({
                    url: getHomeUrl() + "user/update",
                    type: "POST",
                    data: fd,
                    processData: false,
                    contentType: false,
                    success: function (response) {
                        console.log(response);

                        if (response.status == 'SUCCESS') {
                            showAlert("alert alert-success", SUCCESS_MESSAGE);
                        } else if (response.status == "INFO") {
                            showAlert("alert alert-info", INFO_MESSAGE);
                        } else if (response.status == "ERROR") {
                            showAlert("alert alert-danger", ERROR_MESSAGE);
                        } else {
                            showAlert("alert alert-warning", WARNING_MESSAGE);
                            for (var i = 0; i < response.errorMessageList.length; i++) {
                                var item = response.errorMessageList[i];
                                $('#' + item.cause).attr("data-content", item.message);
                                $('#' + item.cause).popover("show");
                            }
                        }
                    },
                    error: function () {
                        showAlert("alert alert-danger", ERROR_MESSAGE);
                    }
                })
            }
        }
    )
});

