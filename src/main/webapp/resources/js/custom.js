function shareOnFacebook() {
	window.location.href = 'https://www.facebook.com/sharer/sharer.php?u='+encodeURIComponent(window.location.href);
};

function shareOnTwitter() {
	window.location.href = 'https://twitter.com/home?status='+encodeURIComponent(window.location.href);
};