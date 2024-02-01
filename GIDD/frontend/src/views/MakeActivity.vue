<template>
  <div id="make-activity">
    <div class="nav">
      <button @click="activityFeed" class="back-button">
        <i class="fa fa-arrow-left" aria-hidden="true"></i>
        Gå tilbake
      </button>
    </div>
    <div>
      <h2>Opprett en ny aktivitet</h2>
      <ImageSelector
        labelName=""
        @imageSelected="onSelectedImage"
        @removeImage="onRemoveImage"
      />
    </div>
    <div id="host-container">
      <h3>Arrangør</h3>
      <div id="host">
        <img
          src="../../src/assets/placeholder-pfp.jpg"
          alt="Applikasjonens logo 'Gidd'"
          height="20"
        />
        <h4>{{ user.forename }} {{ user.surname }}</h4>
      </div>
    </div>
    <div>
      <h3>Tittel</h3>
      <input
        v-model="activity.title"
        type="title"
        placeholder="Oppgi beskrivende tittel"
      />
      <div class="error-message-container">
        <p class="error-message" v-if="!validTitle">
          Oppgi en tittel til aktiviteten
        </p>
      </div>
    </div>
    <div>
      <h3>Startdato</h3>
      <div id="start-date-container">
        <select v-model="selectedYear" name="year">
          <option hidden disabled value>Velg år</option>
          <option
            v-for="(year, index) in availableYears"
            :value="year"
            :key="index"
          >
            {{ year }}
          </option>
        </select>
        <select v-model="selectedMonth" name="month">
          <option hidden disabled value>Velg måned</option>
          <option
            v-for="(month, index) in months"
            :value="month.name"
            :key="index"
          >
            {{ month.name }}
          </option>
        </select>
        <select v-model="selectedDay" name="day">
          <option hidden disabled value>Velg dato</option>
          <option
            v-for="index in daysInCurrentMonth"
            :value="index"
            :key="index"
          >
            {{ index }}
          </option>
        </select>
      </div>
    </div>
    <div>
      <h3>Start-tidspunkt</h3>
      <div id="start-time-container">
        <select v-model="selectedHour" name="hour">
          <option hidden disabled value>Velg time</option>
          <option v-for="index in hoursList" :value="index" :key="index">
            {{ index }}
          </option>
        </select>
        <select v-model="selectedMinute" name="minutes">
          <option hidden disabled value>Velg minutt</option>
          <option v-for="index in minutes" :value="index" :key="index">
            {{ index }}
          </option>
        </select>
      </div>
      <div class="error-message-container">
        <p class="error-message" v-if="!validDateTime">Oppgi riktig starttid</p>
      </div>
    </div>
    <div>
      <h3>Oppgi varighet</h3>
      <h5>Oppgi tid i antall timer</h5>
      <input
        v-model="durationHour"
        type="durationHour"
        placeholder="Varighet timer"
      />
      <div class="error-message-container">
        <p class="error-message" v-if="!validDuration">
          Oppgi varighet på aktiviteten
        </p>
      </div>
    </div>
    <div>
      <h3>Aktivitiet</h3>
      <input v-model="activity.type" type="type" placeholder="Type aktivitet" />
      <div class="error-message-container">
        <p class="error-message" v-if="!validType">Oppgi aktivitetstype</p>
      </div>
    </div>
    <div>
      <h3>Antall deltakere</h3>
      <input
        v-model="participants"
        type="maxParticipants"
        placeholder="Maks antall deltakere"
      />
      <div class="error-message-container">
        <p class="error-message" v-if="!validMaxParticipants">
          Oppgi maks antall deltakere
        </p>
      </div>
    </div>
    <div>
      <h3>Belastningsnivå</h3>
      <h5>Hva slags belastningsnivå er aktiviteten?</h5>
      <div id="fitness-level-container">
        <div>
          <input v-model="isEasy" type="checkbox" id="easy" name="easy" />
          <label for="easy">Lett</label><br />
        </div>
        <div>
          <input v-model="isMedium" type="checkbox" id="medium" name="medium" />
          <label for="medium">Medium</label><br />
        </div>
        <div>
          <input v-model="isHard" type="checkbox" id="hard" name="hard" />
          <label for="hard">Høy</label><br />
        </div>
      </div>
      <div class="error-message-container">
        <p class="error-message" v-if="!validDifficulty">
          Oppgi en vanskelighetsgrad
        </p>
      </div>
    </div>
    <div>
      <h3>Sted</h3>
      <h5>Velg sted ved å trykke på kartet eller fyll ut info</h5>
      <div id="map-view">
        <Map id="map" :getLocation="true" :activityData="[]"></Map>
      </div>
    </div>
    <div>
      <div>
        <h5>Oppgi sted</h5>
        <div id="place-container">
          <input v-model="activity.place" type="place" placeholder="Sted" />
          <input v-model="activity.city" type="city" placeholder="By" />
          <div class="error-message-container">
            <p class="error-message" v-if="!validPlaceCity">Oppgi et sted</p>
          </div>
        </div>
      </div>
    </div>
    <div>
      <h3>Beskrivelse</h3>
      <h5>Legg til en kort beskrivelse av aktiviteten (frivillig)</h5>
      <textarea
        id="description"
        v-model="activity.description"
        type="description"
        placeholder="Beskrivelse"
      />
    </div>
    <div>
      <h3>Utstyr</h3>
      <h5>
        Legg til utstyr som trengs for å gjennomføre aktiviteten (frivillig)
      </h5>
      <input
        v-model="activity.equipment"
        type="equipment"
        placeholder="Utstyr"
      />
    </div>
    <p v-if="feedbackError">Noe gikk galt, prøv igjen!</p>
    <p v-if="feedbackMissingInfo">
      Sjekk at du har fylt inn all nødvendig informasjon
    </p>
    <p v-if="feedbackSomethingWentWrong">Noe gikk galt, prøv igjen</p>
    <button @click="makeActivity">Opprett aktivitet</button>
  </div>
</template>

<script lang="ts">
import axios from "../axiosConfig";
import {
  computed,
  defineComponent,
  onBeforeMount,
  reactive,
  Ref,
  ref,
  provide,
  watch,
} from "vue";
import { useRouter } from "vue-router";
import MakeActivity from "@/interfaces/Activity/MakeActivity.interface";
import { useStore } from "@/store";
import Month from "../interfaces/Month.interface";
import Map from "@/components/Map.vue";
import ICoordinates from "@/interfaces/ICoordinates.interface";
import ILocation from "@/interfaces/ILocation.interface";
import { TrainingLevel } from "@/enums/TrainingLevel.enum";
import ImageSelector from "@/components/ImageSelector.vue";
import axiosNotConfig from "axios";
import data from "@/../config.json";

export default defineComponent({
  components: { ImageSelector, Map },
  setup() {
    const apiKey = data.googleAPIKey;
    const durationHour = ref("");
    const isEasy = ref(false);
    const isMedium = ref(false);
    const isHard = ref(false);
    const selectedYear = ref("");
    const selectedMonth = ref("");
    const selectedDay = ref("");
    const currentYear = new Date().getFullYear();
    const limitForUpperYear = 10;
    const selectedHour = ref("");
    const selectedMinute = ref("");
    const user = ref({});
    const router = useRouter();
    const store = useStore();
    const feedbackError = ref(false);
    enum difficultyValue {
      LOW = TrainingLevel.LOW,
      MEDIUM = TrainingLevel.MEDIUM,
      HIGH = TrainingLevel.HIGH,
    }
    const participants = ref("");
    const feedbackMissingInfo = ref(false);
    const feedbackSomethingWentWrong = ref(false);
    const coordinates = reactive({ lat: 0.0, lng: 0.0 } as ICoordinates);
    provide("coordinates", coordinates);

    //Activity object
    const activity = reactive({
      title: "",
      type: "",
      description: "",
      equipment: "",
      difficulty: -1,
      city: "",
      place: "",
      longitude: 0,
      latitude: 0,
      startTime: "",
      durationMinutes: -1,
      privateActivity: false,
      maxParticipants: -1,
      activityPicture: "",
    } as MakeActivity);

    const daysInFebruary = computed(() => {
      return isLeapYear.value ? 29 : 28;
    });

    const months: Ref<Month[]> = ref([
      { name: "Januar", numberOfDays: 31 },
      { name: "Februar", numberOfDays: daysInFebruary },
      { name: "Mars", numberOfDays: 31 },
      { name: "April", numberOfDays: 30 },
      { name: "Mai", numberOfDays: 31 },
      { name: "Juni", numberOfDays: 30 },
      { name: "Juli", numberOfDays: 31 },
      { name: "August", numberOfDays: 31 },
      { name: "September", numberOfDays: 30 },
      { name: "Oktober", numberOfDays: 31 },
      { name: "November", numberOfDays: 30 },
      { name: "Desember", numberOfDays: 31 },
    ]);

    //List of hours to use in dropdown menu - to get "0" in front of single digits
    const hoursList = ref([
      "00",
      "01",
      "02",
      "03",
      "04",
      "05",
      "06",
      "07",
      "08",
      "09",
      "10",
      "11",
      "12",
      "13",
      "14",
      "15",
      "16",
      "17",
      "18",
      "19",
      "20",
      "21",
      "22",
      "23",
    ]);

    //Same as hours, but with minutes
    const minutes = [
      "00",
      "01",
      "02",
      "03",
      "04",
      "05",
      "06",
      "07",
      "08",
      "09",
    ];

    const updateCityPlace = async () => {
      try {
        const response = await axiosNotConfig
          .get(
            `https://maps.googleapis.com/maps/api/geocode/json?latlng=${
              coordinates.lat + "," + coordinates.lng
            }&key=${apiKey}`
          )
          .then();
        const responseData = response.data;
        if (response.status == 200) {
          let address: string[] = (responseData.results[0]
            .formatted_address as string).split(",");
          let place = address[0];
          let city = address[1].split(" ");
          place != "Unnamed Road"
            ? (activity.place = place)
            : (activity.place = ""); //Setting the place value
          city = city.filter((element) => {
            //Filters away city names that is not valid
            if (element == "") {
              return false;
            }
            if (!isNaN(Number(element))) {
              return false;
            }
            return true;
          });

          city[0] || city[0] != "Unnamed"
            ? (activity.city = city[0])
            : (activity.city = ""); //Setting the city value
        }
      } catch (error) {
        //Something went wrong, user has to write place and city
      }
    };

    watch(
      () => coordinates.lat || coordinates.lng,
      (newValue, oldValue) => {
        if (newValue != oldValue) {
          updateCityPlace();
        }
      }
    );

    //Before page loads, make the rest of the minutes list
    onBeforeMount(() => {
      let n = "0";
      for (let i = 10; i < 60; i++) {
        n = String(i);
        // eslint-disable-next-line vue/no-side-effects-in-computed-properties
        minutes.push(n);
      }
      return minutes;
    });

    /**
     * Method to make the activity once the button has been pressed.
     * Starts by getting the correct information to the last attributes
     * in the activity object, then tries to post the activity to backend.
     * If the activity form is not filled correctly or posting fails,
     * feedback will be showed or the user will be sent to the error page.
     * If successful, the user will be sent to the activity page of the
     * just made activity
     */
    const makeActivity = async (): Promise<void> => {
      activity.difficulty = calculateDifficulty.value;
      activity.durationMinutes = parseFloat(durationHour.value) * 60.0;
      activity.maxParticipants = parseInt(participants.value);
      activity.startTime = makeDateTime.value;
      if (coordinates.lat != 0.0 || coordinates.lng != 0.0) {
        activity.latitude = coordinates.lat;
        activity.longitude = coordinates.lng;
      }
      try {
        if (!isActivityInvalid.value) {
          const response = await axios.post("/activities", activity);
          await axios.get(`/activities/` + response.data.activityId);
          router.push(`/activity/` + response.data.activityId);
        } else {
          feedbackMissingInfo.value = true;
        }
      } catch {
        if (isActivityInvalid.value) {
          feedbackMissingInfo.value = false;
          feedbackSomethingWentWrong.value = true;
        }
      }
    };

    /**
     * Method to turn the date and time into correct format
     */
    const makeDateTime = computed(() => {
      return (
        selectedYear.value +
        "-" +
        getMonthNumber.value +
        "-" +
        getDay.value +
        " " +
        selectedHour.value +
        ":" +
        selectedMinute.value
      );
    });

    /**
     * Method to add "0" on single days for date and time format
     */
    const getDay = computed(() => {
      if (parseInt(selectedDay.value) < 10) {
        return "0" + selectedDay.value;
      }
      return selectedDay.value.toString();
    });

    /**
     * Method to get the months corresponding number for date and time format
     */
    const getMonthNumber = computed(() => {
      if (selectedMonth.value === "Januar") {
        return "01";
      } else if (selectedMonth.value === "Februar") {
        return "02";
      } else if (selectedMonth.value === "Mars") {
        return "03";
      } else if (selectedMonth.value === "April") {
        return "04";
      } else if (selectedMonth.value === "Mai") {
        return "05";
      } else if (selectedMonth.value === "Juni") {
        return "06";
      } else if (selectedMonth.value === "Juli") {
        return "07";
      } else if (selectedMonth.value === "August") {
        return "08";
      } else if (selectedMonth.value === "September") {
        return "09";
      } else if (selectedMonth.value === "Oktober") {
        return "10";
      } else if (selectedMonth.value === "November") {
        return "11";
      } else if (selectedMonth.value === "Desember") {
        return "12";
      }
      return "Noe gikk galt";
    });

    const validPlaceCity = computed(() => {
      return activity.place !== "" && activity.city !== "";
    });

    const validDifficulty = computed(() => {
      return (
        activity.difficulty !== -1 ||
        isEasy.value ||
        isMedium.value ||
        isHard.value
      );
    });

    const validMaxParticipants = computed(() => {
      return (
        participants.value !== "" &&
        !isNaN(Number(participants.value)) &&
        Number(participants.value) &&
        !participants.value.includes("-")
      );
    });

    const validType = computed(() => {
      return activity.type !== "";
    });

    const validDuration = computed(() => {
      return (
        durationHour.value !== "" &&
        !isNaN(Number(durationHour.value)) &&
        Number(durationHour.value) > 0 &&
        !durationHour.value.includes("-")
      );
    });

    const validDateTime = computed(() => {
      return (
        selectedYear.value !== "" ||
        selectedMonth.value !== "" ||
        selectedDay.value !== "" ||
        selectedHour.value !== "" ||
        selectedMinute.value !== ""
      );
    });

    const validTitle = computed(() => {
      return activity.title !== "";
    });

    const calculateDifficulty = computed(() => {
      let difficultyNumber = 0;
      if (isEasy.value) {
        difficultyNumber += difficultyValue.LOW;
      }
      if (isMedium.value) {
        difficultyNumber += difficultyValue.MEDIUM;
      }
      if (isHard.value) {
        difficultyNumber += difficultyValue.HIGH;
      }
      return difficultyNumber;
    });

    const isActivityInvalid = computed(() => {
      return (
        activity.title === "" ||
        activity.startTime === "" ||
        activity.durationMinutes === -1 ||
        isNaN(activity.durationMinutes) ||
        activity.durationMinutes < 0 ||
        activity.type === "" ||
        activity.maxParticipants === -1 ||
        isNaN(activity.maxParticipants) ||
        activity.maxParticipants < 0 ||
        activity.difficulty === 0 ||
        activity.place === "" ||
        activity.city === ""
      );
    });

    /**
     * Gets user from backend to display name on the page
     */
    onBeforeMount(async () => {
      try {
        const response = await axios.get(`/users/${store.getters.user.userId}`);
        user.value = response.data;
      } catch {
        router.push("/error");
      }
    });

    /**
     * Method to find the available years the user can pick
     */
    const availableYears = computed(() => {
      const years = [];
      for (let i = currentYear + limitForUpperYear; i > currentYear - 1; i--) {
        years.push(i);
      }
      return years.reverse();
    });

    /**
     * Method to return a boolean depending
     * on if the year selected is a leap year
     */
    const isLeapYear = computed(() => {
      if (parseInt(selectedYear.value) === 0) return false;
      return (
        parseInt(selectedYear.value) % 400 === 0 ||
        (parseInt(selectedYear.value) % 100 !== 0 &&
          parseInt(selectedYear.value) % 4 === 0)
      );
    });

    /**
     * Find the days in the selected month
     */
    const daysInCurrentMonth = computed(() => {
      if (selectedMonth.value === "") {
        return 0;
      }
      return months.value.find(
        (month: { name: string }) => month.name === selectedMonth.value
      )?.numberOfDays;
    });

    /**
     * When image is selected
     */
    const onSelectedImage = (image: string) => {
      activity.activityPicture = image;
    };

    /**
     * When image is removed
     */
    const onRemoveImage = () => {
      delete activity.activityPicture;
    };

    const activityFeed = (): void => {
      router.push("/activity-feed");
    };

    return {
      makeActivity,
      user,
      activity,
      availableYears,
      daysInCurrentMonth,
      isLeapYear,
      selectedDay,
      selectedYear,
      selectedMonth,
      months,
      selectedHour,
      selectedMinute,
      isHard,
      isMedium,
      isEasy,
      durationHour,
      feedbackError,
      validTitle,
      validDateTime,
      validDuration,
      validType,
      validMaxParticipants,
      validDifficulty,
      validPlaceCity,
      participants,
      hoursList,
      minutes,
      feedbackMissingInfo,
      feedbackSomethingWentWrong,
      onSelectedImage,
      onRemoveImage,
      activityFeed,
    };
  },
});
</script>

<style scoped lang="scss">
$primary-color: #282828;
$secondary-color: #ea4b4b;
$padding: 0.6rem 1rem 0.6rem 1rem;

#make-activity {
  margin: 35px;
  @media only screen and (min-width: 600px) {
    width: 45%;
    margin: auto;
    padding: 20px;
  }
}

.nav {
  margin-bottom: 10px;
  height: 20px;
}

h2 {
  text-align: center;
}

h3,
h5 {
  text-align: left;
}

h5 {
  font-weight: 500;
}

input {
  width: 100%;
}

img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  margin: 20px;
}

#host {
  display: grid;
  grid-template-rows: 1fr 0.5fr;
  justify-items: center;
}

#host.h4 {
  text-align: left;
}

select {
  width: 100%;
  height: 2rem;
  border-radius: 10px;
  border-width: 0;
  border-bottom: 2px RGBA(0 0 0/4%) solid;
  font-family: "Mulish", sans-serif;
  font-size: 1rem;
  text-align-last: center;
  color: #54545e;
}

#start-date-container {
  margin-bottom: 30px;
}

#start-date-container,
#start-time-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  column-gap: 30px;
}

#fitness-level-container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
}

#fitness-level-container input {
  width: unset;
  margin: 10px;
}

#map {
  padding-top: 20px;
  position: absolute;
  width: 100%;
  left: 0px;
  height: 25%;
  @media only screen and (min-width: 600px) {
    height: 350px;
  }
}

#map-view {
  color: $primary-color;
  height: 270px;
  margin: 35px;
  @media only screen and (min-width: 600px) {
    width: 45%;
    margin: auto;
  }
}

#map {
  height: 250px;
}

#place-container {
  display: grid;
  grid-template-rows: 1fr 1fr;
  row-gap: 15px;
}

#description {
  height: 150px;
  width: 90%;
  align-content: flex-start;
  border: 1px solid #9f9f9f;
  border-radius: 20px;
  padding: $padding;
  font: normal 1rem "mulish", sans-serif;
  color: $primary-color !important;
}

.error-message-container {
  height: 30px;
}

.error-message {
  font-weight: 600;
  color: $secondary-color;
  font-size: 10px;
  line-height: 30px;
  margin: 0px;
}

button {
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
  padding: 0;
  float: left;
}

.back-button:hover {
  color: $secondary-color;
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
  margin: 0px;
}

.back-button:hover {
  color: $secondary-color;
}
</style>
