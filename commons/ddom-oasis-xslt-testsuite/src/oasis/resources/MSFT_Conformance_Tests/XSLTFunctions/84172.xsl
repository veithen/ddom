<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="xml" omit-xml-declaration="yes" indent="yes"/>  
<xsl:template match="/">

<xsl:value-of select="element-available('xsl:transform')"/>

</xsl:template>

</xsl:stylesheet>