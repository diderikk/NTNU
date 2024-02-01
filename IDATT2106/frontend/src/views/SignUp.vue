<template>
  <div id="sign-up">
    <span @click="prevPage" class="back-button">
      <i class="fa fa-arrow-left" aria-hidden="true"></i>
      {{ buttonBackNames }}
    </span>
    <div id="page-one" v-if="stage === 1">
      <h2>Opprett bruker</h2>
      <div id="sign-up-form-container">
        <input
          class="sign-up-form-input"
          type="epost"
          placeholder="E-post"
          v-model="user.email"
        />
        <input
          class="sign-up-form-input"
          type="fornavn"
          placeholder="Fornavn"
          v-model="user.forename"
        />
        <input
          class="sign-up-form-input"
          type="etternavn"
          placeholder="Etternavn"
          v-model="user.surname"
        />
        <div id="sign-up-form-birthday-container">
          <h4>Fødselsdato</h4>
          <div id="sign-up-form-birthday">
            <div id="birthday-year" class="birthday-form">
              <select class="dropdown" v-model="selectedYear">
                <option value="0" hidden disabled>Velg år</option>
                <option
                  v-for="(year, index) in availableYears"
                  :value="year"
                  :key="index"
                >
                  {{ year }}
                </option>
              </select>
            </div>
            <div id="birthday-month" class="birthday-form">
              <select name="month" v-model="selectedMonth">
                <option value="0" selected disabled hidden>Velg måned</option>
                <option
                  v-for="(month, index) in availableMonths"
                  :value="index + 1"
                  :key="index"
                >
                  {{ month.name }}
                </option>
              </select>
            </div>
            <div id="birthday-day" class="birthday-form">
              <select name="day" v-model="selectedDay">
                <option value="0" selected disabled hidden>Velg dato</option>
                <option
                  v-for="index in availableDays"
                  :key="index"
                  :value="index"
                >
                  {{ index }}
                </option>
              </select>
            </div>
          </div>
        </div>
        <!-- Maybe change name to since its not a birthday form -->
        <div class="fitness-form">
          <h4>Treningsnivå</h4>
          <div id="fitness-form-boxes">
            <label
              v-for="(trainingLevel, index) in trainingLevels"
              :key="index"
            >
              <input
                id="fitness-form-input"
                type="radio"
                :value="trainingLevel.value"
                :checked="trainingLevel.value === selectedTrainingLevel"
                @change="changeTrainingLevel(trainingLevel.value)"
              />{{ trainingLevel.title }}
            </label>
          </div>
        </div>
      </div>
      <div id="conditions-container">
        <ul>
          <li v-if="!isEmailValid">! Epost må inneholde både @ og .</li>
          <li v-if="!isNameValid">
            ! Navn må inneholde både fornavn og etternavn
          </li>
          <li v-if="!isBirthDateValid">
            ! Fødselsdato må inneholde år, måned og dato
          </li>
          <li v-if="!isTrainigLevelValid">! Treningsnivå må være valgt</li>
        </ul>
      </div>
    </div>

    <div id="page-two" v-else-if="stage === 2">
      <h2>Skriv inn passord</h2>
      <div id="sign-up-form-container">
        <input
          class="sign-up-form-input"
          type="password"
          v-model="user.password"
          placeholder="Passord"
        />
        <input
          class="sign-up-form-input"
          type="password"
          v-model="repeatPassword"
          placeholder="Gjenta passord"
        />
        <div id="conditions-container">
          <span> {{ passwordFeedback }} </span>
        </div>
      </div>
    </div>

    <div id="pageThree" v-else-if="stage === 3">
      <ImageSelector
        labelName="Select your profile picture"
        @imageSelected="onSelectedImage"
        @removeImage="onRemoveImage"
      />
    </div>

    <div id="page-four" v-else-if="stage === 4">
      <h2>Godta betingelser</h2>
      <div class="terms-and-conditions-container">
        Dette er terms and conditions. Lorem ipsum dolor sit amet, consectetur
        adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore
        magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco
        laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor
        in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla
        pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa
        qui officia deserunt mollit anim id est laborum.
      </div>
      <input
        type="checkbox"
        name="checkbox"
        value="check"
        id="agree"
        v-model="termsAndConditions"
      />
      Jeg godtar brukervilkårene
    </div>

    <div id="wrong" v-else>
      <h1>Something went wrong</h1>
    </div>
    <div id="navigation">
      <button
        @click="nextPage"
        class="router-link-button"
        :disabled="disableNextButton"
      >
        {{ nextButtonNames }}
      </button>

      <Page-Dots
        :maxStageAllowed="maxStageAllowed"
        :numberOfButtons="4"
        :currentStage="stage"
        @changeStage="changeStage($event)"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed, Ref, ref, reactive } from "vue";
import Month from "../interfaces/Month.interface";
import { useRouter } from "vue-router";
import { useStore } from "@/store";
import SignUpUser from "../interfaces/User/SignUpUser.interface";
import ImageSelector from "@/components/ImageSelector.vue";
import PageDots from "../components/PageDots.vue";
import { TrainingLevel } from "@/enums/TrainingLevel.enum";

export default defineComponent({
  name: "SignUp",
  components: {
    PageDots,
    ImageSelector,
  },
  setup() {
    //overall
    const router = useRouter();
    const store = useStore();
    const stage = ref(1);

    /**
     * Gets name of back button based on the stage we're
     */
    const buttonBackNames = computed(() => {
      if (stage.value === 1) return "Avbryt";
      return "Tilbake";
    });

    /**
     * Gets name of next button based on the stage we're
     */
    const nextButtonNames = computed(() => {
      if (stage.value === 4) {
        return "Registrer";
      } else return "Neste";
    });

    /**
     * Finds out if we're going to the disable the next-button
     */
    const disableNextButton = computed(() => {
      return (
        disableNextButtonStageOne.value ||
        disableNextButtonStageTwo.value ||
        disableNextButtonStageFour.value
      );
    });

    /**
     * Decides what action happens when pressing the next button, based on the current stage
     */
    const nextPage = () => {
      if (stage.value < 4) {
        stage.value++;
      } else if (stage.value === 4) {
        saveUser();
      }
    };

    /**
     * Decides what action happens when pressing the back-button, based on the current stage
     */
    const prevPage = () => {
      if (stage.value > 1) {
        stage.value--;
      } else if (stage.value === 1) {
        router.push("/");
      }
    };

    //Methods for stage one
    const selectedYear = ref(0);
    const selectedMonth = ref(0);
    const selectedDay = ref(0);
    const currentDate = new Date();
    const yearOfLowestAge = computed(() => currentDate.getFullYear() - 16);
    const limitForLowerYear = 120;

    /**
     * Returns an array of the years to be displayed, sorted from nearest to furthest away
     */
    const availableYears = computed(() => {
      const years = [];
      for (
        let i = yearOfLowestAge.value - limitForLowerYear;
        //Has to be <= to allow the yearOfLowestAge to be able to equal selectedYear, so that we can correctly calculate the correct age limit
        i <= yearOfLowestAge.value;
        i++
      ) {
        years.push(i);
      }
      return years.reverse();
    });

    const isLeapYear = computed(() => {
      if (selectedYear.value === 0) return false;
      return (
        selectedYear.value % 400 === 0 ||
        (selectedYear.value % 100 !== 0 && selectedYear.value % 4 === 0)
      );
    });

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

    const numberOfAvailableMonths = computed(() => {
      return selectedYear.value === yearOfLowestAge.value
        ? currentDate.getMonth() + 1
        : 12;
    });

    /**
     * Returns an array of the months available to be displayed, based on the numberOfAvailableMonths variable
     */
    const availableMonths = computed(() => {
      return numberOfAvailableMonths.value === 12
        ? months.value
        : months.value.slice(0, numberOfAvailableMonths.value);
    });

    /**
     * Returns the number of available days in the current year and month, with the nearest day possible being 16 years ago
     */
    const availableDays = computed(() => {
      //If the defaultvalue is selected we exit immediatly
      if (selectedMonth.value === 0) return;
      //Have to add - 1 since our dropdownlist uses 1-12 for months and 0 for default, but JS date uses 0-11
      return selectedMonth.value - 1 === currentDate.getMonth() &&
        months.value[selectedMonth.value - 1].numberOfDays >
          currentDate.getDate() &&
        selectedYear.value === yearOfLowestAge.value
        ? currentDate.getDate()
        : months.value[selectedMonth.value - 1].numberOfDays;
    });

    /**
     * Email is valid if it includes "@" and "."
     */
    const isEmailValid = computed(() => {
      return user.email.includes("@") && user.email.includes(".");
    });

    /**
     * Name is valid if forename and surname contains anything else than spaces
     */
    const isNameValid = computed(() => {
      return user.forename.trim() !== "" && user.surname.trim() !== "";
    });

    /**
     * Birth date is valid if selected years value is not 0 (default value), selected months value is not 0 (default value) and selected days value is not 0 (default value)
     */
    const isBirthDateValid = computed(() => {
      return (
        selectedYear.value > 0 &&
        selectedMonth.value > 0 &&
        selectedDay.value > 0
      );
    });

    //Training level
    const trainingLevels = ref([
      { title: "Lav", value: TrainingLevel.LOW },
      { title: "Medium", value: TrainingLevel.MEDIUM },
      { title: "Høy", value: TrainingLevel.HIGH },
    ]);

    const selectedTrainingLevel = ref(0);

    /**
     * Sets thes users form attribute
     */
    const changeTrainingLevel = (value: number) => {
      selectedTrainingLevel.value = value;
    };

    /**
     * Training level is valid if users form attribute
     */
    const isTrainigLevelValid = computed(() => {
      return selectedTrainingLevel.value != 0;
    });

    /**
     * Gets the training value as a string
     * Has to do this because of the REST api taking a string as training level
     */
    const trainingLevelAsString = computed(() => {
      switch (selectedTrainingLevel.value) {
        case 1:
          return "EASY";
        case 2:
          return "MEDIUM";
        case 4:
          return "HARD";
        default:
          return "";
      }
    });

    /**
     * Stage one is complete if email is valid, name is valid, birth date is valid and the training level is valid
     */
    const isStageOneCompleted = computed(() => {
      return (
        isEmailValid.value &&
        isNameValid.value &&
        isBirthDateValid.value &&
        isTrainigLevelValid.value
      );
    });

    /**
     * Disables the next button if the stage is 1, and stage 1 is not completed
     */
    const disableNextButtonStageOne = computed(() => {
      return stage.value === 1 && !isStageOneCompleted.value;
    });

    //methods for stage two
    const repeatPassword = ref("");

    /**
     * Checks if password is equal to or above 8 characters
     */
    const isPasswordSecure = computed((): boolean => {
      return user.password.length >= 8;
    });

    const doesPasswordsMatch = computed((): boolean => {
      return user.password === repeatPassword.value;
    });

    /**
     * Creates password feedback based on lack of equality with the repeated password or 8 character minimum limit is reached
     */
    const passwordFeedback = computed(() => {
      if (!isPasswordSecure.value) {
        return "Passordet må være minst 8 tegn";
      } else if (!doesPasswordsMatch.value) {
        return "Passordene er ikke like";
      }
      return "";
    });

    /**
     * Stage two is completed when stage one is completed, and the created password rechead minimum limit of 8 characters, and is equal to the repeated password
     */
    const isStageTwoCompleted = computed(() => {
      return (
        isStageOneCompleted.value &&
        isPasswordSecure.value &&
        doesPasswordsMatch.value
      );
    });

    /**
     * Disables next button if stage is equal to 2, and stage 2 is not completed
     */
    const disableNextButtonStageTwo = computed((): boolean => {
      return stage.value === 2 && !isStageTwoCompleted.value;
    });

    //methods for stage three
    const onSelectedImage = (imageData: string) => {
      user.profilePicture = imageData;
    };

    const onRemoveImage = () => {
      delete user.profilePicture;
    };

    //methods for stage four
    const termsAndConditions = ref(false);
    /**
     * Stage four is completed if stage two is completed and the terms and conditions are checked
     */
    const isStageFourCompleted = computed(() => {
      return isStageTwoCompleted.value && termsAndConditions.value === true;
    });

    /**
     * Disables next button if stage is equal to 4, and stage 4 is not completed
     */
    const disableNextButtonStageFour = computed((): boolean => {
      return stage.value === 4 && !isStageFourCompleted.value;
    });

    //methods for connection to backend
    const birthdate = computed(() => {
      return (
        selectedYear.value +
        "-" +
        (selectedMonth.value < 10 ? "0" : "") +
        selectedMonth.value +
        "-" +
        (selectedDay.value < 10 ? "0" : "") +
        selectedDay.value
      );
    });

    const user = reactive({
      email: "",
      password: "",
      forename: "",
      surname: "",
      trainingLevel: "",
    } as SignUpUser);

    /**
     * Saves the user by dispatching the register action in our store instance
     * Transers user to the activity feed if the register action is successful
     * Pushes the user to /error if something goes wrong
     */
    const saveUser = async (): Promise<void> => {
      user.dateOfBirth = birthdate.value;
      user.trainingLevel = trainingLevelAsString.value;
      if (await store.dispatch("register", user)) {
        router.replace("/welcome");
      } else {
        router.push("/error");
      }
    };

    /**
     * Calculates the max stage the user is allowed to be in based on the completion of other stages
     */
    const maxStageAllowed = computed(() => {
      if (isStageTwoCompleted.value) return 4;
      if (isStageOneCompleted.value) return 2;
      return 1;
    });

    /**
     * Changes stage to the number give
     */
    const changeStage = (index: number) => {
      stage.value = index;
    };

    return {
      //stage one
      availableYears,
      availableMonths,
      availableDays,
      selectedYear,
      selectedMonth,
      selectedDay,
      isNameValid,
      isEmailValid,
      isBirthDateValid,
      disableNextButton,
      trainingLevels,
      isTrainigLevelValid,
      changeTrainingLevel,
      selectedTrainingLevel,

      //stage two
      passwordFeedback,
      repeatPassword,
      onRemoveImage,

      //stage three
      onSelectedImage,

      //stage four
      isStageFourCompleted,
      termsAndConditions,

      //overall
      buttonBackNames,
      nextButtonNames,
      nextPage,
      prevPage,
      stage,
      user,
      saveUser,
      maxStageAllowed,
      changeStage,
    };
  },
});
</script>

<style scoped lang="scss">
$primary-color: #282828;
$secondary-color: #ea4b4b;
$disabled-color: #d7dce6;
$padding: 0.6rem 1rem 0.6rem 1rem;

h4 {
  margin: 0;
}

h5 {
  margin: 0;
  padding-right: 13px;
}

#sign-up {
  margin: 35px;
  top: 0px;
  height: 90%;
  overflow: scroll;
  text-align: left;
  color: $primary-color;
  padding-bottom: 100px;
  @media only screen and (min-width: 600px) {
    width: 30%;
    margin: auto;
    margin-top: 0px;
  }
}

#sign-up-form-container {
  display: grid;
  grid-auto-rows: 3rem 3rem 3rem 6rem;
  margin-bottom: 20px;
}

.sign-up-form-container-picture {
  text-align: center;
}

.sign-up-form-input {
  width: 100%;
  border-radius: 0;
  border-width: 0;
  border-bottom: 2px #d7dce6 solid;
}

.sign-up-form-input:active,
.sign-up-form-input:focus {
  border: none;
  box-shadow: none;
  border-bottom: 5px $secondary-color solid;
}

#sign-up-form-birthday-container {
  display: grid;
  grid-template-rows: auto;
  margin: 20px 0px 20px 0px;
}

#sign-up-form-birthday {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  margin-top: 15px;
  column-gap: 15px;
}

.birthday-form {
  width: 100%;
  text-align: center;
}

.fitness-form {
  margin-top: 10px;
  width: 100%;
}

#fitness-form-boxes {
  text-align: center;
  margin: 15px 0px 15px 0px;
}

#fitness-form-input {
  margin: 10px;
}

select {
  width: 100%;
  height: 2rem;
  border-radius: 0;
  border-width: 0;
  border-bottom: 2px $disabled-color solid;
  font-family: "Mulish", sans-serif;
  font-size: 1rem;
  text-align-last: center;
  color: #54545e;
}

option {
  height: 30px;
}

select:focus {
  box-shadow: none;
}

#conditions-container {
  padding-top: 25px;
  padding-bottom: 15px;
  height: 60px;
  font-weight: 600;
  color: $secondary-color;
  font-size: 10px;
  line-height: 20px;
}

ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
}

#profile-picture {
  width: 150px;
  border-radius: 50%;
  background-color: #999;
  align-items: center;
  justify-content: center;
  justify-self: center;
}

.picture-upload-submit {
  margin-top: 15px;
  font-weight: 600;
}

#picture-input {
  width: 100%;
}

#navigation {
  text-align: center;
  position: fixed;
  padding-top: 10px;
  padding-bottom: 30px;
  bottom: 0px;
  left: 0;
  right: 0;
  background-color: #ffffff;
}

button:disabled {
  background-color: $disabled-color;
  color: #9499a5;
}

.back-button {
  color: $primary-color;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: 600;
  font-size: 0.7rem;
  width: 60px;
  text-align: center;
}

.back-button:hover {
  color: $secondary-color;
}

.terms-and-conditions-container {
  overflow-y: scroll;
  font-size: 0.8rem;
  line-height: 1rem;
  height: 200px;
  margin-bottom: 20px;
}

::-webkit-scrollbar {
  width: 0;
  background: transparent;
}

::-webkit-scrollbar-thumb {
  background: #ff0000;
}
</style>
