<template>
  <div id="activity-information">
     <div id="nav">
    <button @click="goBack" class="back-button">
      <i class="fa fa-arrow-left" aria-hidden="true"></i>
      Gå tilbake
    </button>
    </div>
    <div id="map">
      <img
        id="map-img"
        :src="
          'https://maps.googleapis.com/maps/api/staticmap?center=' +
          activity.latitude +
          ',' +
          activity.longitude +
          '&zoom=14&size=600x350&markers=color:blue%7Clabel:A%7C' +
          activity.latitude +
          ',' +
          activity.longitude +
          '&key=' +
          apiKey
        "
        alt="Illustrasjon som viser et utsnitt fra google maps der aktiviteten skal være"
      />
    </div>
    <h2 id="activity-title">{{ activity.title }}</h2>
    <div id="host">
      Arrangør
      {{ activityOrganizer.forename }} {{ activityOrganizer.surname }}
      <img
        v-if="activityOrganizer.trusted"
        id="verified"
        src="../../img/verified.png"
        height="15"
      />
    </div>
    <div id="weather" v-if="dataReceived">
      <Weather
        :latitude="activity.latitude"
        :longitute="activity.longitude"
        :time="activity.startTime"
      />
    </div>
    <div v-else>Ingen værdata</div>
    <div id="information-wrapper">
      <label class="event-variable">Når</label>
      <div class="variable-value">{{ dateTimeFormatter }}</div>
      <label class="event-variable">Varighet</label>
      <div class="variable-value">{{ durationFormatter }}</div>
      <label class="event-variable">Hvor</label>
      <div class="variable-value">
        {{ activity.place }}, {{ activity.city }}
      </div>
      <label class="event-variable">Hva</label>
      <div class="variable-value">{{ activity.type }}</div>
      <label class="event-variable">Belastning</label>
      <div class="variable-value">{{ difficulty }}</div>
      <label class="event-variable">Deltakere</label>
      <div class="variable-value">
        {{ numberOfParticipants }} / {{ activity.maxParticipants }}
      </div>
    </div>

    <div id="signing-up-wrapper">
      <div id="signing-up" v-if="signedUp">
        <div id="signing-up-conformation" v-if="!isOrganizer">
          Du er påmeldt!
        </div>
        <div id="signing-up-conformation" v-else>Du er arrangør!</div>
        <button
          @click="signOffActivity"
          alt="Knapp for å melde seg av en aktivitet"
          v-if="!isOrganizer"
        >
          Meld deg av
        </button>
        <div v-else>
          <button @click="edit">Rediger</button>
          <button @click="toRegisterAbsence">Registrer fravær</button>
        </div>
        <button
          @click="openChat"
          alt="Knapp for å chatte med andre på samme aktivitet"
        >
          Chat
        </button>
      </div>
      <div v-else>
        <button
          @click="signUpActivity"
          alt="Knapp for å melde seg på en aktivitet"
        >
          Meld deg på
        </button>
        <p v-if="loading">Laster inn ...</p>
      </div>
    </div>
    <div class="details-wrapper">
      <h3>Beskrivelse</h3>
      <p>{{ activity.description }}</p>
    </div>
    <div class="details-wrapper">
      <h3>Utstyr</h3>
      <p>{{ activity.equipment }}</p>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onBeforeMount, Ref, ref } from "vue";
import { useRouter } from "vue-router";
import axios from "../axiosConfig";
import data from "@/../config.json";
import { store } from "../store";
import IActivity from "@/interfaces/IActivity.interface";
import getActivityDifficultyName from "../utils/getActivityDifficultyName";
import Weather from "../components/Weather.vue";
import User from "../interfaces/User/User.interface";

export default defineComponent({
  name: "ActivityInformation",
  props: {
    id: {
      required: true,
      type: String,
    },
  },
  components: {
    Weather,
  },
  setup(props) {
    //General methods
    const activityOrganizer: Ref<User> = ref({
      userId: -1,
      forename: "",
      surname: "",
      email: "",
      score: -1,
      rating: -1,
      role: "",
      dateOfBirth: "",
      profilePicture: "",
      trainingLevel: "",
      trusted: false,
    });
    const router = useRouter();
    const apiKey = data.googleAPIKey;
    const activity: Ref<IActivity> = ref({
      activityId: -1,
      organizerId: -1,
      title: "",
      description: "",
      equipment: "",
      difficulty: -1,
      city: "",
      place: "",
      longitude: 0,
      latitude: 0,
      startTime: "0000-00-00 00:00",
      durationMinutes: 0,
      maxParticipants: 0,
      type: "",
      privateActivity: false,
      activityPicture: "",
      chatId: -1,
      organizerForename: "Gidd",
      organizerSurname: "",
    } as IActivity);
    const signedUp = ref(false);
    const loading = ref(false);

    const participants = ref([]); //list with all participants
    const dataReceived = ref(false);

    /**
     * Method for getting number of participants on an activity
     */
    const numberOfParticipants = computed((): number => {
      if (signedUp.value) return participants.value.length + 1;
      return participants.value.length;
    });

    /**
     * Method for signing off an activity
     */
    const signOffActivity = async (): Promise<void> => {
      try {
        await axios.delete(
          `users/${store.getters.user.userId}/activities/${props.id}`
        );
        signedUp.value = false;
        await checkServerForUpdate();
      } catch (error) {
        router.push("/error");
      }
    };

    const getChatId = computed(() => {
      return activity.value.chatId;
    });
    /**
     * Method for signing up to an activity
     */
    const signUpActivity = async (): Promise<void> => {
      try {
        await axios.post(
          `/activities/${props.id}/users/${store.getters.user.userId}`
        );
        signedUp.value = true;
        await checkServerForUpdate();
      } catch (error) {
        router.push("/error");
      }
    };

    /**
     * Opens chat
     */
    const openChat = (): void => {
      router.push(`/activity/${activity.value.activityId}/chat`);
    };

    /**
     * Checks if the loggen on user is the organizer of an activity
     */
    const isOrganizer = computed((): boolean => {
      return store.getters.user.userId === activity.value.organizerId;
    });

    /**
     * Button to edit activity
     */
    const edit = () => {
      router.push(`/edit-activity/${activity.value.activityId}`);
    };

    const checkServerForUpdate = async () => {
      //eve
      try {
        loading.value = true;
        //gets the info from backend
        const response = axios.get(`/activities/${props.id}`);
        const participantResponse = axios.get(`/activities/${props.id}/users`);
        const signedUpResponse = axios.get(
          `/activities/${props.id}/users/${store.getters.user.userId}`
        );

        //collects all the data and collects in array

        const res = await Promise.all([
          response,
          participantResponse,
          signedUpResponse,
        ]);

        //assignes the data to the right value
        activity.value = res[0].data;
        participants.value = res[1].data;
        signedUp.value = res[2].data;
        loading.value = false;
      } catch (error) {
        router.push("/error");
      }
    };

    //connection
    /**
     * Connects to backend using a get request to get the activity
     */
    onBeforeMount(async () => {
      try {
        await checkServerForUpdate();
        if (activity.value.organizerId > 0) {
          const organizerResponse = await axios.get(
            `/users/${activity.value.organizerId}`
          );
          activityOrganizer.value = organizerResponse.data;
        } else {
          activity.value.organizerForename = "Gidd";
        }
        dataReceived.value = true;
      } catch (error) {
        router.push("/error");
      }
    });

    /**
     * Routes the owner of the activity to register absence
     */
    const toRegisterAbsence = (): void => {
      router.push(`/activity/${props.id}/register-absence`);
    };

    /**
     * Routes user to the previous page
     */
    const goBack = () => {
      router.back();
    };

    /**
     * Formats the duration to show hours and minutes
     */
    const durationFormatter = computed(() => {
      if (activity.value.durationMinutes > 60) {
        const timeHour = ref(Math.floor(activity.value.durationMinutes / 60));
        const extraMin = ref(activity.value.durationMinutes % 60);
        if (activity.value.durationMinutes % 60 === 0) {
          return timeHour.value + " timer";
        }
        return timeHour.value + " timer og " + extraMin.value + " minutter";
      }
      return activity.value.durationMinutes + " minutter";
    });

    /**
     * Formats the date and time
     */
    const dateTimeFormatter = computed(() => {
      const temp = activity.value.startTime.split(" ");
      const dateArray = temp[0].split("-");
      const date = ref(dateArray[2] + "/" + dateArray[1] + "/" + dateArray[0]);
      const time = ref(temp[1]);
      return date.value + " kl. " + time.value;
    });

    /**
     * Calculates the difficulty of the activity as a string
     */
    const difficulty = computed(() => {
      return getActivityDifficultyName(activity.value.difficulty || 0);
    });

    return {
      durationFormatter,
      dateTimeFormatter,
      toRegisterAbsence,
      signUpActivity,
      signOffActivity,
      difficulty,
      edit,
      isOrganizer,
      activity,
      openChat,
      goBack,
      activityOrganizer,
      apiKey,
      numberOfParticipants,
      loading,
      signedUp,
      dataReceived,
    };
  },
});
</script>

<style scoped lang="scss">
$primary-color: #282828;
$secondary-color: #ea4b4b;

#activity-information {
  margin: 10px 35px 10px 35px;
  text-align: left;
  @media only screen and (min-width: 600px) {
    width: 45%;
    margin: auto;
  }
}

#nav-back {
  margin-bottom: 20px;
  font-size: 1rem;
}

#nav-back:hover,
#nav-back:active {
  color: $secondary-color;
}

#map-img {
  border-radius: 20px;
  width: 100%;
  @media only screen and (min-width: 600px) {
    width: 100%;
    height: auto;
  }
}

#activity-title {
  margin: 20px 0px 20px 0px;
  text-align: center;
}

#host {
  font-weight: 600;
  margin: 10px 0px 20px 0px;
}

#information-wrapper {
  display: grid;
  grid-template-columns: 1fr 3fr;
  grid-auto-rows: 2rem;
  width: 100%;
  text-align: left;
  align-items: center;
  img {
    margin: -2px auto;
  }
}

.event-variable {
  grid-column: 1/2;
  font-weight: 700;
}

.variable-value {
  grid-column: 2/3;
}

#signing-up-wrapper {
  margin: 20px 0px 20px 0px;
  justify-content: center;
  text-align: center;
}

#signing-up-conformation {
  margin-bottom: 10px;
  font-weight: 700;
}

button {
  text-transform: uppercase;
  color: #ffffff;
  font-weight: 600;
  margin: 0px 5px 0px 5px;
}

.details-wrapper {
  margin: 20px 0px 20px 0px;
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
  padding: 10px 0px 10px 0px;
  display: block;
}

.back-button:hover {
  color: $secondary-color;
}
</style>
