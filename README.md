# MatchMate

Kotlin, XML Views + ViewBinding, MVVM.

## What it does

A list of match cards pulled from randomuser.me, each with a photo, name, age and
location, and Accept / Decline buttons. Deciding on a card replaces the buttons with a
"Member Accepted" or "Member Declined" pill and writes the decision to the database, so it
survives scrolling, rotation and restarts.

More cards load as you approach the end of the list, with a spinner in the footer while a
page is in flight.

Everything is served from Room, so the app works with no connection — cached cards are
still listed and still decidable. A banner shows while you're offline, and when the
connection comes back the list refreshes and a toast confirms it. First launch with no
network shows a message and a Retry button instead of an empty screen.

## Libraries

| | |
| --- | --- |
| Retrofit + Gson | API calls and JSON parsing |
| Room | local storage, and the single source of truth for the UI |
| Glide | profile photos, with a placeholder while loading and on failure |
| LiveData | what the Activity observes |
| Coroutines + Flow | everything below the ViewModel |
| Material Components | cards, buttons, toolbar |
| RecyclerView + ConstraintLayout | the list and both layouts |

## Running it

Open the project in Android Studio and run the `app` module — no API key or local config
needed, randomuser.me is unauthenticated.

From the command line:

```
./gradlew installDebug
```

The Gradle daemon runs on JDK 21 (`gradle/gradle-daemon-jvm.properties`); compilation
targets Java 11 through a toolchain, which Gradle resolves or downloads on its own. Gradle
8.11.1 via the wrapper, AGP 8.7.3.

Targets Android 15 (API 35), minimum API 28.

## Project structure

All code sits under `app/src/main/kotlin/com/blahblah/matchmate/`.

```
connectivity/   network callbacks, exposed as a Flow<Boolean>
container/      AppContainer — hand-rolled DI (no Hilt for a one-screen app)
interfaces/     the contracts each layer talks through
models/         Profile for the UI, ProfileDto for the API, MatchStatus for decisions
repository/     ListProfilesRepository, plus local/ (Room) and remote/ (Retrofit)
ui/
  recyclerview/ adapters, view holders, diffing, and the pagination trigger
  screen/       BaseActivity + BaseViewModel, and the profile listing screen itself
utils/          DTO -> entity -> domain mapping
```

Room is the source of truth. The Activity observes the ViewModel, the ViewModel combines
load state with whatever Room currently holds, and the repository is the only thing that
talks to the network. Nothing reads the API directly, which is what makes accept/decline
work with no connection.

Pagination lives in `BaseViewModel` rather than the screen — the adapter asks for the next
page as it binds near the end of the list, and the ViewModel decides whether that request
is allowed. A `Job` handle is what stops overlapping requests, so there's no separate
in-flight flag to keep in sync.

The connectivity observer is shared once and derived twice: the offline banner needs the
current value, while the "back online" toast must only fire on a real transition and must
not replay when the Activity restarts. Those are different requirements, so they're
separate flows off one upstream.

A note on the API: randomuser.me returns new random users on every call and has no write
endpoint, so "sync when the connection returns" can't mean pushing decisions upstream. It
refreshes the list instead. Decisions stay local, keyed by the user's `uuid`, and survive
a refresh — profiles without a uuid get dropped on the way in, since there'd be no stable
key to attach a decision to.
