import React from 'react';
import { View,Text,Button} from "react-native";
import { Card} from 'react-native-elements'

const About = ({ navigation }) => (
  <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
    <Card>
      <Card.Title>关于本app</Card.Title>
      <Card.Divider />
      <Text style={{ marginBottom: 10 }}>
        作为智能软件终端2020秋学期大作业，由浙江大学计算机科学与技术学院信息安全专业
        王祚滨和王国朝开发维护
      </Text>
      
      <Button
        title='返回主界面'
        onPress={() => {
          navigation.navigate('Home');
        }} />
    </Card>
  </View>
);

export default About;

