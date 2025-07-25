# Contributing to BTAndroidApp

Contributions are welcome to `BTAndroidApp` project!
Your help is invaluable in making this app better for everyone. To ensure a smooth and effective
collaboration, please follow these guidelines carefully.

---

## Our Contribution Workflow

To maintain project quality and coordination, we follow a specific workflow for contributions:

1. **Open an Issue First:**
    * Before starting any work, please **always open an issue** to discuss the bug, feature, or
      change you intend to work on.
    * This step is crucial for ensuring that the work aligns with project goals and avoids duplicate
      efforts.

2. **Discuss the Issue:**
    * We will discuss the issue with you to clarify requirements, approach, and feasibility.
    * Please participate actively in the discussion on the issue tracker.

3. **Wait for Approval to Work:**
    * Only after we have discussed the issue and explicitly ask you to proceed with implementing
      a solution, should you start coding.
    * This ensures that your valuable time is spent on features or fixes that the project needs and
      approves.

4. **Create a Dedicated Branch:**
    * Once approved to work, please create a new branch from `main` using one of the following
      conventions:
        * For new features: `feat/<descriptive_feature_name>`
        * For bug fixes: `bug/<bug-decription>`

---

## General Guidelines

- **Code of Conduct:** Please review and adhere to our [Code of Conduct](CODE_OF_CONDUCT.md) in all
  your interactions within this project.
- **Pull Requests:**
    - When submitting your code, please
      use [Pull Request Template](/.github/PULL_REQUEST_TEMPLATE.md) and fill it out completely.
    - Link your pull request to the issue you worked on (e.g., `Closes #123`).
    - Be prepared for constructive feedback during the review process.

## Setting up the Development Environment

If you're unsure how to get the project running locally, here are the basic steps:

1. **Prerequisites:**
    * Java Development Kit (JDK) 17 or higher.
    * Android Studio (latest stable version recommended).
    * Android SDK Platform 34 (or the target API level of the app).

2. **Clone the repository:**
   ```bash
   git clone https://github.com/tuuhin/BTAndroidApp.git
   cd BTAndroidApp
   ```

3. **Open in Android Studio:**
   Open the cloned project in Android Studio. Android Studio should guide you through syncing Gradle
   files and downloading dependencies.

4. **Build and Run:**
   Ensure the project builds successfully (`./gradlew assembleDebug`) and runs on an emulator or
   physical device.

---

Thank you for your understanding and for contributing to the `BTAndroidApp`!