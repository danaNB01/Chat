'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector("#usernameForm");
var messageForm = document.querySelector("#messageForm");
var messageInput = document.querySelector("#message");
var messageArea = document.querySelector("#messageArea");
var connectingElemenet = document.querySelector(".connecting");

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];


// For usernameForm
function connect(event) {
    event.preventDefault();

    username = document.getElementById('name').value.trim();
    if (username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        // to connect

        // 1 creates web socket connection to server endpoint /ws
        var socket = new SockJS('/ws');

        // 2 wrap the websocket object with stomp protocol
        stompClient = Stomp.over(socket);

        // 3 establish stomp connection over the websocket
        // {} headers passed during connection, callback, callback
        stompClient.connect({}, onConnected, onError);

    }
}


function onConnected(options) {
    // sub to public topic --> the destination where the server broadcasts msgs.
    stompClient.subscribe('/topic/public', onMessageReceived);

    // send a msg to the server indicating that a new user has joined the chat.
    // url defined in controller
    // {} no additional headers
    // msg payload, contains info used by server to add user to session and notify others.
    stompClient.send('/app/chat.addUser',
        {},
        JSON.stringify({sender: username, type: 'JOIN'}));

    // hide the connecting... block.
    connectingElemenet.classList.add('hidden');
}


function onError() {
    connectingElemenet.textContent = 'Could not connect to WebSocket. Please refresh the page and try again!';
    connectingElemenet.style.color = 'red';
}

// whenever a msg is published to the topic 'by the server', this function handles the incoming msg.
// this is where the chat UI with received msg is updated.
function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    console.log('inside onMessageReceived: ' , message.sender);
    console.log('inside onMessageReceived: ' , message.type);
    console.log('inside onMessageReceived: ' , message.content);

    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    }else{
        messageElement.classList.add('chat-message');

        // create an avatar
        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);


        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}
// -------------------------

// For messageForm

function sendMessage(event){

    var messageContent = messageInput.value.trim();
    // also check the connection again before sending.
    if(messageContent && stompClient){
        var chatMessage = {
            sender: username,
            content: messageContent,
            type: 'CHAT'
        };
        stompClient.send(
            '/app/chat.sendMessage',
            {},
            JSON.stringify(chatMessage)
        );
        messageInput.value = '';
    }

    event.preventDefault()
}

// ---------------------------------------------

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
