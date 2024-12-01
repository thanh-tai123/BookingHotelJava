import { ip, port } from '/ConfigJs/config.js';

const chatbotToggler = document.querySelector(".chatbot-toggler");
const closeBtn = document.querySelector(".close-btn");
const chatbox = document.querySelector(".chatbox");
const chatInput = document.querySelector(".chat-input textarea");
const sendChatBtn = document.querySelector(".chat-input span");
var eR = false;
let userMessage = null; // Variable to store user's message
const API_KEY = "PASTE-YOUR-API-KEY"; // Paste your API key here
const inputInitHeight = chatInput.scrollHeight;

const createChatLi = (message, className) => {
    const chatLi = document.createElement("li");
    chatLi.classList.add("chat", className);
    let chatContent = className === "outgoing"
        ? `<div></div>`
        : `<span><img src="/images/logo.png" alt="icon" width="30" height="30"></span><div></div>`;
    chatLi.innerHTML = chatContent;
    chatLi.querySelector("div").textContent = message;
    return chatLi;
}


const generateResponse = (chatElement, userMessage) => {
    const API_URL = "http://" + ip + ":" + port + "/chat-ai";
    const messageElement = chatElement.querySelector("div");

    // Tạo URL với tham số message
    const urlWithParams = `${API_URL}?message=${encodeURIComponent(userMessage)}`;

    // Định nghĩa các tùy chọn yêu cầu
    const requestOptions = {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        }
    };

    // Gửi yêu cầu GET đến API và xử lý phản hồi theo kiểu stream
    fetch(urlWithParams, requestOptions)
        .then(response => {
            // Kiểm tra phản hồi có trạng thái thành công (status code từ 200-299)
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }

            // Kiểm tra xem phản hồi có body không
            if (!response.body) {
                throw new Error("No response body");
            }

            const reader = response.body.getReader();
            const decoder = new TextDecoder("utf-8");

            let result = '';

            // Hàm xử lý từng phần dữ liệu stream
            const processStream = ({ done, value }) => {
                if (done) {
                    console.log("Finished streaming.");
                    //sendMessage(result, '123');  // Gửi toàn bộ kết quả cuối cùng sau khi stream xong

                    // Thêm thuộc tính target="_blank" cho tất cả các liên kết trong nội dung
                    const links = messageElement.querySelectorAll('a');
                    links.forEach(link => {
                        link.setAttribute('target', '_blank');
                    });

                    // Thêm lớp 'zoomable' vào tất cả các thẻ <img> và bọc chúng trong thẻ <a> để kích hoạt phóng to
                    const images = messageElement.querySelectorAll('img');
                    images.forEach(img => {
                        const imgSrc = img.src;
                        // Tạo thẻ <a> bao quanh thẻ <img>
                        const wrapper = document.createElement('a');
                        wrapper.href = imgSrc;
                        wrapper.classList.add('image-popup');
                        // Chèn thẻ <img> vào trong thẻ <a>
                        img.parentNode.insertBefore(wrapper, img);
                        wrapper.appendChild(img);
                    });

                    // Khởi tạo Magnific Popup cho các thẻ <a> đã được tạo ở trên
                    $('.image-popup').magnificPopup({
                        type: 'image',
                        closeOnContentClick: true,
                        mainClass: 'mfp-img-mobile',
                        image: {
                            verticalFit: true
                        }
                    });

                    return;
                }

                // Giải mã phần dữ liệu nhận được và cập nhật giao diện
                const chunk = decoder.decode(value, { stream: true });
                result += chunk;

                const htmlContent = marked.parse(result);
                messageElement.innerHTML = htmlContent;  // Cập nhật nội dung từng phần
                chatbox.scrollTo(0, chatbox.scrollHeight);
                // Đọc phần tiếp theo của stream
                return reader.read().then(processStream);
            };

            // Bắt đầu đọc dữ liệu stream
            return reader.read().then(processStream);
        })
        .catch(error => {
            // Xử lý lỗi và hiển thị thông báo
            console.error("Error occurred:", error); // Ghi log chi tiết lỗi ra console
            messageElement.classList.add("error");

            // Hiển thị thông báo lỗi tùy theo lỗi cụ thể
            if (error.message.includes("Failed to fetch")) {
                messageElement.textContent = "Không thể kết nối đến máy chủ. Vui lòng kiểm tra kết nối mạng hoặc thử lại sau.";
            } else if (error.message.includes("HTTP error")) {
                messageElement.textContent = "OOps! Có lỗi xảy ra. Vui lòng thử lại.";
            } else {
                messageElement.textContent = "Oops! Có lỗi xảy ra. Vui lòng thử lại.";
            }
        })
        .finally(() => {
            // Cuộn xuống cuối cùng của chatbox
            chatbox.scrollTo(0, chatbox.scrollHeight);
        });
};



const handleChat = () => {
    userMessage = chatInput.value.trim(); // Get user entered message and remove extra whitespace
    if (!userMessage) return;

    // Clear the input textarea and set its height to default
    chatInput.value = "";
    chatInput.style.height = `${inputInitHeight}px`;

    // Append the user's message to the chatbox
    chatbox.appendChild(createChatLi(userMessage, "outgoing"));
    chatbox.scrollTo(0, chatbox.scrollHeight);
    if (eR == false) {
        setTimeout(() => {
            // Display "Thinking..." message while waiting for the response
            const incomingChatLi = createChatLi("Đang soạn...", "incoming");
            //sendMessageUser(userMessage);
            chatbox.appendChild(incomingChatLi);
            chatbox.scrollTo(0, chatbox.scrollHeight);
            generateResponse(incomingChatLi, userMessage);
        }, 600);
    } else {
        //sendMessageUser(userMessage);
    }
}

chatInput.addEventListener("input", () => {
    // Adjust the height of the input textarea based on its content
    chatInput.style.height = `${inputInitHeight}px`;
    chatInput.style.height = `${chatInput.scrollHeight}px`;
});

chatInput.addEventListener("keydown", (e) => {
    // If Enter key is pressed without Shift key and the window 
    // width is greater than 800px, handle the chat
    if (e.key === "Enter" && !e.shiftKey && window.innerWidth > 800) {
        e.preventDefault();
        handleChat();
    }
});

function convertTextToLink(text) {
    // Biểu thức chính quy để tìm URL, bao gồm các ký tự Unicode
    const urlPattern = /(\b(https?|ftp|file):\/\/[^\s<>"']+)/ig;

    return text.replace(urlPattern, function (url) {
        return `<a href="${url}" target="_blank">Tại đây</a>`;
    });
}

sendChatBtn.addEventListener("click", handleChat);
closeBtn.addEventListener("click", () => document.body.classList.remove("show-chatbot"));
chatbotToggler.addEventListener("click", () => document.body.classList.toggle("show-chatbot"));

var stompClient = null;

// function connect() {
//     // Khởi tạo SockJS với URL server Spring Boot
//     // Tạo đối tượng SockJS để kết nối server qua http://localhost:8080/ws
//     var socket = new SockJS('http://' + ip + ':' + port + '/ws');
//     // Tạo đối tượng STOMP từ SockJS
//     stompClient = Stomp.over(socket);
//     // Tạo kết nối từ stompClient đến server 
//     stompClient.connect({}, function (frame) {
//         // Hiển thị thông tin kết nối trong bảng điều khiển Console
//         console.log('Connected: ' + frame);
//         // Đăng ký để nhận tin nhắn từ server
//         stompClient.subscribe('/topic/public', function (messageOutput) {
//             showMessage(JSON.parse(messageOutput.body));
//         });
//     }, function (error) {
//         // Hiển thị lỗi trong bảng điều khiển nếu có
//         alert("Có lỗi kết nối!");
//         console.error('Connection error: ', error);
//     });
// }

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect(function () {
            alert("Đã ngắt kết nối!");
            console.log("Disconnected");
        });
    }
}

function sendMessage(AiResponse, id) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
            'id': id,
            'content': AiResponse,
            'sender': 'ChatBot',
            'timestamp': new Date()
        }));
    } else {
        alert("Lỗi gửi tin nhắn!");
        console.error('Connection not established. Message not sent.');
    }
}

function sendMessageUser(userMessage) {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
            'content': userMessage,
            'sender': 'User',
            'timestamp': new Date()
        }));
    } else {
        alert("Lỗi gửi tin nhắn!");
        console.error('Connection not established. Message not sent.');
    }
}

function showMessage(message) {
    if (message.sender == 'Employee') {
        if (message.responding == true) {
            eR = true;
            const incomingChatLi = createChatLi(message.content, "incoming");
            chatbox.appendChild(incomingChatLi);
            chatbox.scrollTo(0, chatbox.scrollHeight);
        } else {
            eR = false;
        }
    }
}

window.onload = function () {
    connect();
};
