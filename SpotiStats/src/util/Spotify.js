import React from 'react';
import ReactDOM from 'react-dom';

import App from '../Components/App/App';

const clientId = '91634a5ff1e447268c56ef98c5373697';
const redirectUri = 'http://localhost:3000/';

class Spotify extends React.Component{
    constructor(props){
        super(props); 
        this.state = {
            token: null
        };
        this.setToken = this.setToken.bind(this);
        this.clearToken = this.clearToken.bind(this);
        this.getToken = this.getToken.bind(this);
        this.searchTrack = this.searchTrack.bind(this);
        this.searchArtist = this.searchArtist.bind(this);
        this.savePlaylist = this.savePlaylist.bind(this);
        this.generatePersonalizedPlaylist = this.generatePersonalizedPlaylist.bind(this);
        this.logout = this.logout.bind(this);
        this.generateRecommendations = this.generateRecommendations.bind(this);
        this.getGenres = this.getGenres.bind(this);
    }

    timeout;

    setToken(token){
        this.setState({token: token})
    }

    clearToken(){
        this.setState({token: null});

    };

    getToken(){
        window.location = `https://accounts.spotify.com/authorize?client_id=${clientId}&response_type=token&scope=playlist-modify-public user-top-read&redirect_uri=${redirectUri}`;
    };

    componentDidMount(){
        const accessTokenMatch = window.location.href.match(/access_token=([^&]*)/);
        const expiresInMatch = window.location.href.match(/expires_in=([^&]*)/);
            if(accessTokenMatch && expiresInMatch){
                this.setToken(accessTokenMatch[1]);
                const expiresIn = expiresInMatch[1];
                console.log(expiresIn);


                this.timeout = setTimeout(this.clearToken, expiresIn * 1000);
                window.history.pushState("Access Token", null, "/");
            }
    };

    componentWillUnmount() {
        if (this.timeout) {
          clearTimeout(this.timeout);
        }
    };

    searchTrack(term, criteria){
        console.log(criteria);
        return fetch(`https://api.spotify.com/v1/search?q=${criteria}:${term}&type=track`, {
            method: 'GET',
            headers: { 
                Authorization: `Bearer ${this.state.token}`
            }
        }).then(response => { 
            return response.json();
        }).then(jsonResponse => {
            if(!jsonResponse.tracks){ 
                return [];
            } 
            return jsonResponse.tracks.items.map(track => ({
                id: track.id,
                name: track.name,
                artist: track.artists[0].name,
                album: track.album.name,
                uri: track.uri
            }))
        });
    };

    searchTrack(term, criteria){
        console.log(criteria);
        return fetch(`https://api.spotify.com/v1/search?q=${criteria}:${term}&type=track`, {
            method: 'GET',
            headers: { 
                Authorization: `Bearer ${this.state.token}`
            }
        }).then(response => {
            return response.json();
        }).then(jsonResponse => {
            if(!jsonResponse.tracks){ 
                return [];
            } 
            return jsonResponse.tracks.items.map(track => ({
                id: track.id,
                name: track.name,
                artist: track.artists[0].name,
                album: track.album.name,
                uri: track.uri
            }))
        });
    };

    searchArtist(term){
        return fetch(`https://api.spotify.com/v1/search?q=${term}&type=artist`, {
            method: 'GET',
            headers: { 
                Authorization: `Bearer ${this.state.token}`
            }
        }).then(response => {
            return response.json();
        }).then(jsonResponse => {
            if(!jsonResponse.artists){ 
                return [];
            } 
            return jsonResponse.artists.items.map(artist => ({
                id: artist.id,
                name: artist.name,
                url: artist.images.length > 0 ? artist.images[0].url : undefined
            }))
        });
    };

    generatePersonalizedPlaylist(timeRange){
        return fetch(`https://api.spotify.com/v1/me/top/tracks?time_range=${timeRange}&limit=50`, {
            method: 'GET',
            headers: {
                Authorization: `Bearer ${this.state.token}`
            }
        }).then(response => {
            return response.json();
        }).then(jsonResponse => {
            if(!jsonResponse.items){ 
                return [];
            } 
            return jsonResponse.items.map(track => ({
                id: track.id,
                name: track.name,
                artist: track.artists[0].name,
                album: track.album.name,
                uri: track.uri
            }))
        });
    };

    generateRecommendations(artists, tracks, genres, urlCriteria){
        let url = 'https://api.spotify.com/v1/recommendations?';
        if(artists.length > 0)
        {
            url += 'seed_artists=';
            for(var i = 0; i < artists.length-1; i++) 
            {
                url += artists[i] + ',';
            } 
            url += artists[i];
        }
        if(genres.length > 0)
        {
            if(artists.length > 0)
            {
                url += '&';
            }
            url += 'seed_genres=';
            for(var i = 0; i < genres.length; i++) 
            {
                url += genres[i] + '%2C';
            } 
        }
        if(tracks.length > 0)
        {
            if(artists.length > 0 || genres.length > 0)
            {
                url += '&';
            }
            url += 'seed_tracks=';
            for(var i = 0; i < tracks.length-1; i++) 
            {
                url += tracks[i] + ',';
            } 
            url += tracks[i];
        }
        if(urlCriteria != undefined){
            url += "&" + urlCriteria;
        }
        console.log('url:' + url);
        console.log('url to search for:' +  urlCriteria);
        return fetch(url, {
            method: 'GET',
            headers: {
                Authorization: `Bearer ${this.state.token}`
            }
        }).then(response => {
            return response.json();
        }).then(jsonResponse => {
            if(!jsonResponse.tracks){ 
                return [];
            } 
            return jsonResponse.tracks.map(track => ({
                id: track.id,
                name: track.name,
                artist: track.artists[0].name,
                album: track.album.name,
                uri: track.uri
            }))
        });
    };

    getGenres(){
        return fetch(`https://api.spotify.com/v1/recommendations/available-genre-seeds`, {
            method: 'GET',
            headers: {
                Authorization: `Bearer ${this.state.token}`
            }
        }).then(response => {
            return response.json();
        }).then(jsonResponse => {
            if(!jsonResponse.genres){ 
                return [];
            } 
            return jsonResponse.genres;
        });
    };

    savePlaylist(name, trackUris){
        if(!name || !trackUris){
            return;
        }
        if(this.state.token == null){
            this.getToken();
        } 
        let userId;

        return fetch('https://api.spotify.com/v1/me', { headers: { Authorization: `Bearer ${this.state.token}`}} 
        ).then(response => response.json()
        ).then(jsonResponse => {
            userId = jsonResponse.id;
             return fetch(`https://api.spotify.com/v1/users/${userId}/playlists`,
             {
                method: 'POST',
                headers: { Authorization: `Bearer ${this.state.token}`},
                body: JSON.stringify({ name: name}) /* playlist name from parameter value */
             }).then(response => response.json()
             ).then(jsonResponse => {
                 const playlistId = jsonResponse.id;
                 return fetch(`https://api.spotify.com/v1/users/${userId}/playlists/${playlistId}/tracks`,
                 {
                     method: 'POST',
                     headers: { Authorization: `Bearer ${this.state.token}`},
                     body: JSON.stringify({ uris: trackUris})
                 })
             })
        });
    }

    logout(){
        window.location = `https://accounts.spotify.com/authorize?client_id=${clientId}&response_type=token&scope=playlist-modify-public user-top-read&redirect_uri=${redirectUri}&show_dialog=true`;
    }

    render() {
        return <App token={this.state.token} 
                    getToken={this.getToken} 
                    searchTrack={this.searchTrack} 
                    searchArtist={this.searchArtist} 
                    savePlaylist={this.savePlaylist}
                    generatePersonalizedPlaylist={this.generatePersonalizedPlaylist}
                    generateRecommendations={this.generateRecommendations}
                    getGenres={this.getGenres}
                    logout={this.logout}/>;
      }
}
export default Spotify;