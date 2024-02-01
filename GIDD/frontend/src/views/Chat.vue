<template>
  <div id="chat">
    <div id="chat-wrapper">
      <div class="nav">
        <button @click="goBack" class="back-button">
          <i class="fa fa-arrow-left" aria-hidden="true"></i>
          Gå tilbake
        </button>
      </div>
      <div class="header">
        <h3>{{ activity.title }}</h3>
      </div>
      <hr />
      <div class="displayMessages" id="displayContainer">
        <div v-if="messages.length > 0">
          <div
            class="messages"
            v-for="(message, index) of messages"
            :key="index"
            :class="{ classForUserMessages: message.userId === user.userId }"
          >
            <Message :messageData="message" />
          </div>
        </div>
      </div>
      <hr />
      <div class="lower">
        <input
          type="message"
          id="messageInput"
          placeholder="Skriv melding her..."
          v-model="messageInput"
          @keyup.enter="sendMessage"
        />
        <button @click="sendMessage">SEND</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  Ref,
  ref,
  onBeforeMount,
  computed,
  onBeforeUnmount,
  onErrorCaptured,
} from "vue";

import { useRouter } from "vue-router";
import IMessage from "@/interfaces/IMessage.interface";
import axios from "@/axiosConfig";
import { useStore } from "@/store";
import Message from "@/components/Message.vue";
import IActivity from "@/interfaces/IActivity.interface";
import Stomp from "stompjs";
import SockJS from "sockjs-client";

export default defineComponent({
  name: "Chat",
  components: {
    Message,
  },
  props: { id: { required: true, type: String } },
  setup(props) {
    const messageInput = ref("");
    const store = useStore();
    const router = useRouter();
    const messages: Ref<IMessage[]> = ref([]);
    const user = computed(() => {
      return store.getters.user;
    });

    let stompClient: Stomp.Client;

    const activity: Ref<IActivity> = ref({} as IActivity);

    onBeforeMount(async () => {
      try {
        const response = await axios.get(`/activities/${props.id}`);
        activity.value = response.data;
        const response2 = await axios.get(`/chats/${chatId.value}/messages`);
        messages.value = response2.data as Array<IMessage>;
      } catch (error) {
        router.push("/error");
      }
      let sockJS = new SockJS("http://localhost:8080/api/v1/websocket");
      stompClient = Stomp.over(sockJS);
      stompClient.debug = () => ({});
      await stompClient.connect(
        {
          Authorization: localStorage.getItem("token"),
        },
        () => {
          stompClient.subscribe(
            `/api/v1/chat/${chatId.value}/messages`,
            (message) => {
              messages.value.push(JSON.parse(message.body));
            }
          );
        },
        (err) => {
          router.push("/error");
        }
      );
    });

    onBeforeUnmount(async () => {
      stompClient.disconnect(() => ({}));
    });

    onErrorCaptured((err) => {
      stompClient.disconnect(() => ({}));
    });

    const forename = computed(() => {
      return store.getters.user.forename;
    });

    const chatId = computed(() => {
      return activity.value.chatId;
    });

    const getDate = computed(() => {
      const now = new Date();
      let date =
        now.getFullYear() +
        "-" +
        (now.getMonth() < 10 ? "0" : "") +
        (now.getMonth() + 1) +
        (now.getDate() < 10 ? "0" : "") +
        "-" +
        now.getDate();
      let time =
        (now.getHours() < 10 ? "0" : "") +
        now.getHours() +
        ":" +
        (now.getMinutes() < 10 ? "0" : "") +
        now.getMinutes();
      return date + " " + time;
    });

    const sendMessage = () => {
      if (messageInput.value === "") return;

      let message: IMessage = {
        userId: user.value.userId,
        forename: user.value.forename,
        chatId: chatId.value,
        message: messageInput.value,
        time: getDate.value,
      } as IMessage;

      stompClient.send(
        `/api/v1/chat/${chatId.value}`,
        {},
        JSON.stringify(message)
      );

      //Removes message from input field, so the user doesnt have to delete/remove their message before writing a new one
      messageInput.value = "";
    };

    const goBack = (): void => {
      router.back();
    };

    /**
     * Retrives messages from the database
     */

    return {
      goBack,
      sendMessage,
      messages,
      user,
      forename,
      messageInput,
      activity,
    };
  },
});
</script>

<style scoped lang="scss">
$primary-color: #282828;
$secondary-color: #ea4b4b;
$padding: 0.6rem 1rem 0.6rem 1rem;

#chat {
  margin: 35px;
  @media only screen and (min-width: 600px) {
    width: 45%;
    margin: auto;
    padding: 20px;
  }
}

#messageInput {
  width: 100%;
}

.messages {
  border: black;
  border-radius: 20px;
  text-align: left;
  margin-left: 0.5%;
}

.classForUserMessages {
  text-align: right;
  margin-right: 0.5%;
}

.displayMessages {
  height: 250px;
  overflow-y: scroll;
  overflow: auto;
  display: flex;
  flex-direction: column-reverse;
}

.back-button {
  color: $primary-color;
  background-color: unset;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: 600;
  font-size: 0.7rem;
  width: 130px;
  text-align: left;
  padding: 0;
  display: block;
}

.back-button:hover {
  color: $secondary-color;
}

button {
  margin: $padding;
}
</style>
