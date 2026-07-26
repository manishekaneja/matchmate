# MatchMate

Android take-home for People's Group.

Kotlin, XML Views + ViewBinding, MVVM.

## Build
Open in Android Studio and run the `app` module.

## Project structure

### Application

| File | Purpose |
| --- | --- |
| `MatchMate.kt` | `Application` subclass (`MatchMateApp`); initialises `AppContainer` with the application context on startup. |

### `connectivity/`

| File | Purpose |
| --- | --- |
| `NetworkConnectivityObserver.kt` | `ConnectivityObserver` implementation; bridges `ConnectivityManager.NetworkCallback` into a `callbackFlow<Boolean>`, tracking the set of networks that currently have validated internet. |
| `NetworkExtensions.kt` | Extensions on `ConnectivityManager` / `NetworkCapabilities` that define "online" as having both `NET_CAPABILITY_INTERNET` and `NET_CAPABILITY_VALIDATED`. |

### `container/`

| File | Purpose |
| --- | --- |
| `AppContainer.kt` | Manual dependency container; lazily builds the Room database, local data source, repository and connectivity observer. Avoids pulling in a DI framework for a single-screen app. |

### `interfaces/`

| File | Purpose |
| --- | --- |
| `ApiService.kt` | Marker interface for Retrofit service types; bounds the generic on `BaseRepository`. |
| `ConnectivityObserver.kt` | Contract for connectivity: a `Flow<Boolean>` stream plus a synchronous `isOnline()` snapshot. |
| `ProfilesObserver.kt` | Repository read/write contract the ViewModel depends on, so it never sees a concrete repository. |
| `RecyclerViewItem.kt` | Supplies a stable `identity` for `DiffUtil` item comparison. |

### `models/`

| File | Purpose |
| --- | --- |
| `match/MatchStatus.kt` | The per-profile decision: `NONE`, `ACCEPTED`, `DECLINED`. |
| `match/MatchStatusConverters.kt` | Room `@TypeConverter`s storing the enum as its name. |
| `profile/Profile.kt` | UI-facing profile model — pre-formatted fields the ViewHolder can bind directly. |
| `profile/ProfileDto.kt` | randomuser.me payload with nested `login` / `name` / `dob` / `location` / `picture` objects; every field nullable because the API makes no guarantees. |
| `profile/ProfilesResponseDto.kt` | Response envelope wrapping `results`. |

### `repository/`

| File | Purpose |
| --- | --- |
| `ListProfilesRepository.kt` | Fetches pages from the API into Room, exposes the cached list, records accept/decline decisions, and holds the reconnect sync. |
| `base/BaseRepository.kt` | Shared Retrofit instance and the `initialFetch` / `paginationFetch` contract subclasses implement. |
| `local/AppDatabase.kt` | Room database declaring the `ProfileEntity` table and status converters. |
| `local/ProfileDao.kt` | Queries: observe all profiles ordered by insertion, count rows, count by status, insert-ignoring-duplicates, update status. |
| `local/ProfileEntity.kt` | The `profiles` table row, keyed by the API's `uuid`, carrying `status` and a `createdAt` used for stable ordering. |
| `local/ProfileLocalDataSource.kt` | Wraps the DAO and moves entity↔domain mapping off the main thread. |
| `remote/ProfileService.kt` | Retrofit endpoint for `GET api/` with `seed`, `page` and `results` query parameters. |

### `ui/recyclerview/`

| File | Purpose |
| --- | --- |
| `adapters/ProfileListingAdapter.kt` | `ListAdapter` of profile cards; also triggers pagination from `onBindViewHolder` once the bound position nears the end. |
| `adapters/PaginationLoaderAdapter.kt` | Single-item footer adapter, concatenated after the list, showing a spinner while the next page loads. |
| `interfaces/PaginationProvider.kt` | Lets the adapter request more data without knowing about the ViewModel. |
| `interfaces/ViewHolderInteractions.kt` | Callback for accept/decline taps, implemented by the Activity. |
| `utils/ProfileListingDiffingHandler.kt` | `DiffUtil.ItemCallback` comparing by `identity`, then by content. |
| `utils/UserActionEnum.kt` | Which button was tapped: `ProfileAccepted` or `ProfileRejected`. |
| `viewholder/ProfileCardViewHolder.kt` | Binds a profile to the card: photo via Glide, name, age/location, and swaps the action buttons for a status pill once decided. |
| `viewholder/PaginationLoaderViewHolder.kt` | Holder for the footer spinner. |

### `ui/screen/`

| File | Purpose |
| --- | --- |
| `base/BaseActivity.kt` | ViewBinding inflation, edge-to-edge insets, and `initViews()` / `initObservers()` hooks. |
| `base/BaseViewModel.kt` | Shared load state and the pagination engine — page cursor, in-flight `Job` guard, and the end-of-data latch. |
| `base/LoadState.kt` | `Loading` / `Loaded` / `Failed` for the initial fetch. |
| `profilelisting/ProfileListingActivity.kt` | The screen: wires the concatenated adapters, observes state, and renders list / loading / empty / error. |
| `profilelisting/ProfileListingViewModel.kt` | Combines load state with the cached profiles into `uiState`, derives the offline flag and reconnect events from one shared connectivity stream, and records decisions. |
| `profilelisting/ProfileListingUiState.kt` | `Loading` / `Content` / `Empty` / `Error` states for the screen. |
| `profilelisting/ProfileListingEvent.kt` | One-shot events consumed once and not replayed on configuration change (currently `BackOnline`). |

### `utils/`

| File | Purpose |
| --- | --- |
| `ProfileMappers.kt` | `ProfileDto` → `ProfileEntity` → `Profile` mapping; drops API rows with no `uuid` since there would be no stable key to attach a decision to. |

### `res/`

| Path | Purpose |
| --- | --- |
| `layout/activity_main.xml` | Screen layout: toolbar, offline banner, list, and the centred progress / empty / error views. |
| `layout/profile_card_layout.xml` | Match card: photo, name, age and location, accept/decline buttons, status pill. |
| `layout/item_pagination_loader.xml` | Footer spinner shown while the next page loads. |
| `values/` | `strings.xml`, `colors.xml`, `dimens.xml`, `themes.xml` — no hardcoded user-facing text or magic dimensions in layouts. |
| `drawable/` | Vector icons for accept/decline, the profile placeholder, and the status pill background. |
