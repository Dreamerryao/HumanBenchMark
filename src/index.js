import React from 'react';
import { View ,Text} from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import SplashScreen from 'react-native-splash-screen';

import { createDrawerNavigator } from '@react-navigation/drawer';
import CustomSidebarMenu from "./routers/CustomSidebarMenu"
import AboutScreen from "./components/About"
import { Button, ThemeProvider } from 'react-native-elements';
const Drawer = createDrawerNavigator();

function HomeScreen({ navigation }) {
  return (
    <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
      <Text>Home Screen!</Text>
      <Button
        onPress={() => navigation.navigate('Notifications')}
        title="Go to notifications"
      />
    </View>
  );
}

function NotificationsScreen({ navigation }) {
  return (
    <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
      <Text>Notification!</Text>
      <Button onPress={() => navigation.goBack()} title="Go back home" />
    </View>
  );
}

class App extends React.Component {
  constructor() {
    super();
  }

  async componentDidMount() {
    SplashScreen.hide();

  }

  render() {
    return (
      <>
        {/* <Provider store={store}> */}
        <ThemeProvider>
        <NavigationContainer>
          <Drawer.Navigator
            drawerContentOptions={{
              // activeTintColor: '#e91e63',
              itemStyle: { marginVertical: 5 },
            }}
            drawerContent={(props) => <CustomSidebarMenu {...props} />}
            initialRouteName="Home">
            <Drawer.Screen name="Home" component={HomeScreen} options={{drawerLabel:"主页"}}/>
            <Drawer.Screen name="Notifications" component={NotificationsScreen} options={{drawerLabel:"排行榜"}}/>
            <Drawer.Screen name="about" component={AboutScreen} options={{drawerLabel:"关于"}}/>
          </Drawer.Navigator>
        </NavigationContainer>
        </ThemeProvider>
        {/* </Provider> */}
      </>
    );
  }
}



export default App;
