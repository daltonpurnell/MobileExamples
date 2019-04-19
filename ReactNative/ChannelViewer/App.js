/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 * By using this code you agree to the Phenix Terms of Service found online here:
 * http://phenixrts.com/terms-of-service.html
 */

import React from 'react';
import {StyleSheet, Text, View} from 'react-native';

import {
  RTCPeerConnection,
  RTCIceCandidate,
  RTCSessionDescription,
  RTCView
} from 'react-native-webrtc';

global = Object.assign(global, {
  RTCPeerConnection,
  RTCIceCandidate,
  RTCSessionDescription
});

import sdk from './node_modules/phenix-web-sdk/dist/phenix-web-sdk-react-native';

sdk.RTC.shim(); // Required

// Your app code

const channelAlias = 'reactNative';

export default class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      webrtcSupported: sdk.RTC.webrtcSupported,
      videoURL: ''
    };
  }

  render() {
    return (
      <View style={styles.container}>
        <Text>{'WebRTC is supported: ' + (this.state.webrtcSupported ? 'YES' : 'NO')}</Text>
        <RTCView style={styles.video} streamURL={this.state.videoURL}/>
      </View>
    );
  }

  componentDidMount() {
    console.log('INFO', 'componentDidMount');

    let myAdminApiProxyClient = new sdk.net.AdminApiProxyClient();
    myAdminApiProxyClient.setBackendUri('https://demo-integration.phenixrts.com/pcast');

    const channelExpress = new sdk.express.ChannelExpress({adminApiProxyClient: myAdminApiProxyClient});

    channelExpress.joinChannel({alias: channelAlias}, (error, response) => {
      if (response && response.channelService) {
        this.channelService = response.channelService;
      }
    }, (error, response) => {
      if (response && response.mediaStream) {
        console.log('VIDEO URL', response.mediaStream.getStream().toURL());
        this.setState({videoURL: response.mediaStream.getStream().toURL()});
      }
    });
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center'
  },
  video: {
    height: 360,
    width: '100%'
  }
});