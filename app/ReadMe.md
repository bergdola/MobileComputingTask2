# Sensor App

## Task 

- Design a sensor reading app 
- Design an activity to show the value of the following sensors: gyroscope, accelerometer
- Implement a service to periodically read the above sensor values
- Keep running in background even if the App is closed
- The activity should bind to the above service to receive updates and display the updates

## Solution

- Foreground service is used and implemented in SensorService
- Rationale: goal is to perform a task that is noticeable by the user, even when they're not directly interacting with the app
- To exchange sensor data between the SensorService and the MainActivity, LiveData is used and implemented in SensorLiveData
- Rational: BroadcastManager is deprecated and LiveData suggested instead, see [BroadcastManagerDeprecated](https://developer.android.com/reference/androidx/localbroadcastmanager/content/LocalBroadcastManager)
- SensorLiveData registers as listener for changes of sensor data (on sensor changed) and sets the updated values
- MainActivity observes LiveData
- no explicit timer implemented
- Rationale: when observing the sensor data, it is possible to state a delay on when to read the next sensor data

