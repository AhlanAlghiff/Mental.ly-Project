function isValidUUID(uuid) {
    const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
    return uuidRegex.test(uuid);
}

const roundToTwoDecimals = (number) => {
    return Math.round(number * 100) / 100;
};

export { isValidUUID, roundToTwoDecimals };